package com.mehmetfatih.mytgram;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PostAdapter extends BaseAdapter {

    List<Post> posts;
    LayoutInflater inflater;

    public PostAdapter(Activity activity, List<Post> posts){
        this.posts = posts;
        inflater = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View rowView;
        rowView = inflater.inflate(R.layout.row, null);
        EditText editTextMessage = rowView.findViewById(R.id.editTextMessage);
        TextView textLocation = rowView.findViewById(R.id.textLocation);
        ImageView imageViewRow = rowView.findViewById(R.id.imageViewRow);

        Post post = posts.get(position);
        editTextMessage.setText(post.getMessage());
        imageViewRow.setImageBitmap(post.getImage());
        if (post.getLocation() != null) {
            textLocation.setText(post.getLocation().getLatitude() + " " + post.getLocation().getAltitude());
        }


        return rowView;
    }
}
