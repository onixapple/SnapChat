package com.example.snapmandatory.repo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.snapmandatory.TaskListener;
import com.example.snapmandatory.Updatable;
import com.example.snapmandatory.Model.Snap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Repository {
    private static Repository repository = new Repository();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public List<Snap> snaps = new ArrayList<>();
    public List<Snap> user_snaps = new ArrayList<>();

    private final String SNAPCHAT = "snapchat_test";

    private String user = "onixapple";
    private Updatable activity;

    public static Repository repo(){
        return repository;
    }

    public void setup(Updatable updatableActivity, List<Snap> snaps_list){
        activity = updatableActivity;
        snaps = snaps_list;

        startListener();
    }

    public void setupUserSnaps(Updatable updatableActivity, List<Snap> snaps_list){
        activity = updatableActivity;
        user_snaps = snaps_list;
        startListener();
    }

    public void startListener(){
        db.collection(SNAPCHAT).addSnapshotListener((values, error) -> {
            snaps.clear();
            user_snaps.clear();
            for (DocumentSnapshot snap: values.getDocuments()){
                System.out.println("Snap: " + snap.toString());
                Object user = snap.get("user");
                Object image_id = snap.get("image_id");
                Object localDateTime = snap.get("localDateTime");
                Bitmap image = null;
                if (image_id != null) {
                    image = downloadRawBitmap(snap.getId(), image_id.toString());
                }
                Snap new_snap = new Snap(snap.getId(), user.toString(), image_id.toString(), image, LocalDateTime.parse(localDateTime.toString()));
                snaps.add(new_snap);
                if (user.toString().equals(this.user)){
                    user_snaps.add(new_snap);
                }
                System.out.println("Snap: " + snap.toString());
                if (new_snap.compareDates() >= 24){
                    deleteSnap(new_snap.getId(), new_snap.getImage_id());
                }
            }
            this.activity.update(null);
        });

    }

    public Snap getSnapWithId(String id) {
        for (Snap snap: snaps) {
            if (snap.getId().equals(id)){
                return snap;
            }
        }
        return null;
    }

    public void downloadBitmap(String imageId, TaskListener taskListener) {
        StorageReference ref = storage.getReference(imageId);
        int max_size = 4 * 1024 * 1024;
        ref.getBytes(max_size).addOnSuccessListener( bytes -> {
            taskListener.receive(bytes);
            System.out.println("Image downloaded OK");
        }).addOnFailureListener(exception -> {
            System.out.println("Error downloading image" + exception);
        });

    }

    public Bitmap downloadRawBitmap(String snapId, String imageId) {
        final Bitmap[] image = new Bitmap[1];
        StorageReference ref = storage.getReference(imageId);
        int max_size = 10 * 1024 * 1024;
        ref.getBytes(max_size).addOnSuccessListener( new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                image[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                getSnapWithId(snapId).setImage(image[0]);

            }
        }).addOnFailureListener(exception -> {
            System.out.println("Error downloading image" + exception);
        });
        if (image[0] != null){
            return image[0];
        }else return null;
    }
    //Important method (Dorin)
    //This method is important because it is responsible for uploading images into the database,
    //an ID is generated and after the image is uploaded a snap Object with the id, user_name, image_id, and date_snap_created fields is stored into a HashMap and sent to the Firestore.
    public void uploadSnap(Bitmap bitmap) {
        UUID uuid = UUID.randomUUID();
        String imageId = uuid.toString();

        StorageReference img_ref = storage.getReference(imageId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        img_ref.putBytes(baos.toByteArray()).addOnCompleteListener(snap -> {
            DocumentReference doc_ref = db.collection(SNAPCHAT).document();
            Map<String, String> document = new HashMap<>();
            document.put("user", user);
            document.put("image_id", imageId);
            document.put("localDateTime", LocalDateTime.now().toString());
            doc_ref.set(document);
        }).addOnFailureListener(exception -> {
            System.out.println("Failed to upload " + exception);
        });
    }



    public void deleteSnap(String documentId, String imageId){
        DocumentReference doc_ref = db.collection(SNAPCHAT).document(documentId);
        doc_ref.delete();
        System.out.println("Snap was deleted");
        StorageReference img_ref = storage.getReference(imageId);
        img_ref.delete();
        System.out.println("Image was deleted");
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}