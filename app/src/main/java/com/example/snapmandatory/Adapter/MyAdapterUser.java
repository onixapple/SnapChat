package com.example.snapmandatory.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.snapmandatory.Model.User;
import com.example.snapmandatory.R;

import java.util.List;

public class MyAdapterUser extends BaseAdapter {
    private List<User> users; // will hold data
    private LayoutInflater layoutInflater; // can "inflate" layout files
    public MyAdapterUser(List<User> users, Context context) {
        this.users = users;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }
    @Override
    public Object getItem(int i) {
        return users.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // make layout .xml file first...
        if(view == null){
            view = layoutInflater.inflate(R.layout.myrowusers, null);
        }
        // LinearLayout linearLayout = (LinearLayout)view;
        TextView textView = view.findViewById(R.id.textViewUsers);
        if(textView != null) {
            textView.setText(users.get(i).getUser()); // later I will connect to the items list
        }
        return textView;
    }
}
