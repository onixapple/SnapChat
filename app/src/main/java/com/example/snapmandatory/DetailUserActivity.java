package com.example.snapmandatory;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.snapmandatory.Model.User;
import com.example.snapmandatory.repo.UserRepo;

public class DetailUserActivity extends AppCompatActivity implements TaskListener{
    private ImageView imageView;
    private Bitmap currentBitmap;
    User currentUser;
    TextView textView;
    //Button click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailuser);
        textView = findViewById(R.id.textViewUserDetail);
        imageView = findViewById(R.id.imageViewUsersDetail);
        String UserId = getIntent().getStringExtra("User");
        currentUser = UserRepo.r().getUserWithId(UserId);
        System.out.println(currentUser.toString());
        textView.setText(currentUser.getUser() + " Snap");
    }
    public void backBtnUserPressed(View view) {
        finish();
    }

    @Override
    public void receive(byte[] bytes) {

    }
}
