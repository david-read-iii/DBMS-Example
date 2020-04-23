package com.read.dbmsexample.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.dbmsexample.Activities.ModifyUserActivity;
import com.read.dbmsexample.Models.User;
import com.read.dbmsexample.R;

import java.util.ArrayList;

public class UsersBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> users;

    /**
     * Defines how User objects in the ArrayList should be adapted to be displayed in a ListView.
     */
    public UsersBaseAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        // Set the layout of a single list item.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_single_item, viewGroup, false);
        }

        // Bring XML elements to Java.
        TextView name = view.findViewById(R.id.name);
        TextView userName = view.findViewById(R.id.username);
        TextView password = view.findViewById(R.id.password);
        TextView role = view.findViewById(R.id.role);

        // Set text inside each view to the attributes of each User object.
        name.setText(context.getString(R.string.format_full_name, users.get(i).getFirstName(), users.get(i).getLastName()));
        userName.setText(users.get(i).getUsername());
        password.setText(users.get(i).getPassword());
        role.setText(users.get(i).getRole());

        // Attach a click listener to each view to start the ModifyUserActivity.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ModifyUserActivity.class);

                // Pass the attributes of the selected User object to this activity.
                intent.putExtra("key", users.get(i).getKey());
                intent.putExtra("firstName", users.get(i).getFirstName());
                intent.putExtra("lastName", users.get(i).getLastName());
                intent.putExtra("username", users.get(i).getUsername());
                intent.putExtra("password", users.get(i).getPassword());
                intent.putExtra("role", users.get(i).getRole());

                context.startActivity(intent);
            }
        });
        return view;
    }
}