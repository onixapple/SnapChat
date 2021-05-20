package com.example.snapmandatory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.snapmandatory.Adapter.MyAdapter;
import com.example.snapmandatory.Model.Snap;
import com.example.snapmandatory.Model.User;
import com.example.snapmandatory.repo.Repository;
import com.example.snapmandatory.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Updatable{
    List<User> users = new ArrayList<>();
    List<Snap> snaps = new ArrayList<>();
    ListView listView;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupListView();
        Repository.repo().setup(this, snaps);
        Repository.repo().startListener();
        UserRepo.r().setupUser(this, users);
        UserRepo.r().startUserListener();
    }
        //Important method (Radu) : this method is important because it displays all the objects in a listView. First it finds the listView Square in the .xml file
        //After the Adapter is created with a list of the items we want to display and it is set on the listView and onClick on each object redirects us to a new page with detailed information.
    private void setupListView() {
        listView = findViewById(R.id.listViewMain);
        myAdapter = new MyAdapter(snaps, this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("snapid", snaps.get(position).getId());
            startActivity(intent);
        }));
    }

    public void accountBtnPressed(View view){
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    public void snapBtnPressed(View view){
        Intent intent = new Intent(MainActivity.this, SnapActivity.class);
        startActivity(intent);
    }

    public void userBtnPressed(View view){
        Intent intent = new Intent(MainActivity.this, UsersActivity.class);
        startActivity(intent);
    }

    @Override
    //Important method 2 (Radu): This method allows our application to change at the same moment we made a change in the database.
    public void update(Object obj) {
        myAdapter.notifyDataSetChanged();
    }

}