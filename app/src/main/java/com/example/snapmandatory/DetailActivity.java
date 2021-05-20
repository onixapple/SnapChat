package com.example.snapmandatory;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.snapmandatory.Model.Snap;
import com.example.snapmandatory.repo.Repository;

public class DetailActivity extends AppCompatActivity implements TaskListener{

    private ImageView imageView;
    private Bitmap currentBitmap;
    Snap currentSnap;
    TextView textView;
    //Button click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        textView = findViewById(R.id.textViewDetailUser);
        imageView = findViewById(R.id.imageViewDetail);
        String snapId = getIntent().getStringExtra("snapid");
        currentSnap = Repository.repo().getSnapWithId(snapId);
        System.out.println(currentSnap.toString());
        textView.setText(currentSnap.getUser() + " Snap");
        currentBitmap = currentSnap.getImage();
        imageView.setImageBitmap(currentBitmap);


    }
    /*Button click= (Button) findViewById(R.id.btncamera);
    imageView = (ImageView) findViewById(R.id.imageViewDetail);

        click.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,0);
        }
    });*/

    public void backBtnPressed(View view) {
        finish();
    }

    @Override
    public void receive(byte[] bytes) {
        currentBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(currentBitmap);
    }
}
