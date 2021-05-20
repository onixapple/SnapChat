package com.example.snapmandatory.repo;

import android.graphics.Bitmap;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.snapmandatory.Model.Snap;
import com.example.snapmandatory.Model.User;
import com.example.snapmandatory.Updatable;
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


public class UserRepo {
    private static UserRepo repo = new UserRepo();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    public List<User> users = new ArrayList<>(); // you could use Note, instead of String
    private final String userDB = "users";
    private Updatable activity;
    public static UserRepo r(){
        return repo;
    }
    public void setupUser(Updatable a, List<User> list){
        activity = a;
        users = list;
        startUserListener();
    }

    public void startUserListener(){
        db.collection(userDB).addSnapshotListener((values, error) ->{
            users.clear();
            assert values != null;
            for(DocumentSnapshot snap: values.getDocuments()){
                Object user = snap.get("User");
                Object id = snap.get("id");
                if(user != null){
                    users.add(new User(snap.getId(),user.toString()));
                }else{
                    System.out.println("nothing found");
                }
                System.out.println("Snap: " + snap.toString());
            }
            // have a reference to MainActivity, and call a update()
            activity.update(null);
        });
    }

    public void addUser(String text) {
        // insert a new note with "new note"
        DocumentReference ref = db.collection(userDB).document();
        Map<String,String> map = new HashMap<>();
        map.put("User", text);
        ref.set(map); // will replace any previous value.
        //db.collection("notes").add(map); // short version
        System.out.println("Done inserting new document " + ref.getId());
    }
    public void updateUser(User user) {
        DocumentReference ref = db.collection(userDB).document(user.getId());
        Map<String,String> map = new HashMap<>();
        map.put("User", user.getUser());
        ref.set(map); // will replace any previous value.
        //ref.update("key", "value"); // for updating single values, instead of the whole document.
        System.out.println("Done updating document " + ref.getId());
    }

    public void sendSnapToUser(String text) {
        UUID uuid = UUID.randomUUID();
        String imageId = uuid.toString();

            DocumentReference doc_ref = db.collection(userDB).document();
            Map<String, String> document = new HashMap<>();
            document.put("user", text);
            document.put("image_id", imageId);
            doc_ref.set(document);
    }
    public User getUserWithId(String id) {
        for (User user: users) {
            if (user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    public User getUserWithUserName(String userName) {
        for (User user: users) {
            if (user.getUser().equals(userName)){
                return user;
            }
        }
        return null;
    }


    public void deleteUser(String id){
        DocumentReference docRef = db.collection(userDB).document(id);
        docRef.delete();
    }
}
