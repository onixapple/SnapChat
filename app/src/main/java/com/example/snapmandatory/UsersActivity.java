package com.example.snapmandatory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snapmandatory.Adapter.MyAdapterUser;
import com.example.snapmandatory.Model.User;
import com.example.snapmandatory.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements Updatable{
    
    List<User> users = new ArrayList<>();
    ListView listView;
    MyAdapterUser myAdapterUser;
    Button addButton;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setupListView();
        setupAddButton();
        UserRepo.r().setupUser(this, users);
    }

    private void setupAddButton() {
        addButton = findViewById(R.id.addUserBtn);
        addButton.setOnClickListener(e ->{
//            items.add("New note " + items.size());
//            myAdapter.notifyDataSetChanged(); // tell the listView to reload data
            getTextForUser();
        });
    }



    private void setupListView() {
        listView = findViewById(R.id.listViewUser);
        myAdapterUser = new MyAdapterUser(users, this);
        listView.setAdapter(myAdapterUser);

    }

    public void getTextForUser() {
        AlertDialog.Builder alert_dialog = new AlertDialog.Builder(this);
        alert_dialog.setTitle("Enter Friend's Name");
        final EditText input_text = new EditText(this);
        input_text.setInputType(InputType.TYPE_CLASS_TEXT);
        alert_dialog.setView(input_text);

        alert_dialog.setPositiveButton("Done", ((dialog, which) -> addUserName(input_text.getText().toString())));
        alert_dialog.setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));

        alert_dialog.show();
    }

    public void addUserName(String text){
        UserRepo.r().addUser(text);
    }

    public void sendToUser(View view){
        getUserNameForSend();
    }

    public void getUserNameForSend(){
        AlertDialog.Builder alert_dialog = new AlertDialog.Builder(this);
        alert_dialog.setTitle(" Write user ");
        final EditText input_text = new EditText(this);
        input_text.setInputType(InputType.TYPE_CLASS_TEXT);
        alert_dialog.setView(input_text);

        alert_dialog.setPositiveButton("Done", ((dialog, which) -> UserRepo.r().sendSnapToUser(input_text.getText().toString())));
        alert_dialog.setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));

        alert_dialog.show();
    }

    public void cancelBtnPressed(View view) {
        finish();
    }

    @Override
    public void update(Object o) {
        myAdapterUser.notifyDataSetChanged();
    }
}
