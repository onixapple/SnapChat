package com.example.snapmandatory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.snapmandatory.repo.Repository;

public class SnapActivity extends AppCompatActivity {

    Bitmap bitmapToUpload;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap);
        imageView = findViewById(R.id.imageViewSnap);
        imageView.setVisibility(View.INVISIBLE);
    }

    public void snapBtnPressed(View view) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePhotoIntent, 1);
        } catch (ActivityNotFoundException ex) {
            System.out.println("Error with activity" + ex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmapToUpload = (Bitmap) extras.get("data");
        }
        getTextForImage();
    }
        //This method is important because it creates an AlertDialog then prompts the user to input the required text for the image,
        // the input type is set to text and afterwards the user has the choice to proceed or cancel his request
    public void getTextForImage() {
        AlertDialog.Builder alert_dialog = new AlertDialog.Builder(this);
        alert_dialog.setTitle("Put some text on the Image");
        final EditText input_text = new EditText(this);
        input_text.setInputType(InputType.TYPE_CLASS_TEXT);
        alert_dialog.setView(input_text);

        alert_dialog.setPositiveButton("Done", ((dialog, which) -> drawTextToBitmap(input_text.getText().toString())));
        alert_dialog.setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));

        alert_dialog.show();
    }


    public void drawTextToBitmap(String text) {
        Bitmap.Config bitmapConfig = bitmapToUpload.getConfig();
        if (bitmapConfig == null){
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmapToUpload = bitmapToUpload.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmapToUpload);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(250, 250, 250));
        paint.setTextSize((int) (20));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        canvas.drawText(text, 5, 120, paint);
        imageView.setImageBitmap(bitmapToUpload);
        imageView.setVisibility(View.VISIBLE);
    }

    public void saveBtnPressed(View view) {
        Repository.repo().uploadSnap(bitmapToUpload);
    }

    public void sendToUser(View view){
        Intent intent = new Intent(this, UsersActivity.class);
        startActivity(intent);
    }



    public void cancelBtnPressed(View view) {
        finish();
    }
}