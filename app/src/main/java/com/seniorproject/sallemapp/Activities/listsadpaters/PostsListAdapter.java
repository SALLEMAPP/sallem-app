package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.seniorproject.sallemapp.Activities.AddEventActivity;
import com.seniorproject.sallemapp.Activities.dbhelpers.DbContext;
import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Centeral on 2/21/2017.
 */

public class PostsListAdapter  extends ArrayAdapter<DomainPost> {
    private ArrayList<DomainPost> _items;
    private Context _adpaterContext;
    public PostsListAdapter(Context context, ArrayList<DomainPost> items) {
        super(context, R.layout.post_layout, items);
        _adpaterContext = context;
        _items = items;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        try {
            DomainPost post = _items.get(position);
            if(v == null){
                LayoutInflater vi =
                        (LayoutInflater) _adpaterContext.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE
                        );
                v = vi.inflate(R.layout.post_layout, null);
            }
            //Bind the UI elements to entity
           bindPost(post, v);

        }
        catch (Exception e){
            //Log the exception so it can be reviewed.
            //Log.d("SALLEM APP", e.getStackTrace().toString());
            throw e;
        }
        return v;
    }

    private void bindPost(DomainPost post, View v) {
        ImageView userAvatart = (ImageView) v.findViewById(R.id.post_layout_user_avatar);
        TextView posDate = (TextView)
                v.findViewById(R.id.post_layout_lbl_post_date);
        TextView poster = (TextView)
                v.findViewById(R.id.post_layout_lbl_user_name);
        TextView postSubject = (TextView)
                v.findViewById(R.id.postlayout_txtPostSubject);
        ImageView postImage = (ImageView)
                v.findViewById(R.id.post_layout_img_post_image);
        Button commentsButton = (Button)
                v.findViewById(R.id.post_layout_btn_comments);
        TextView quickComment = (TextView)
                v.findViewById(R.id.postlayout_txtPostCom);

        poster.setText(post.get_user().getFirstName() + " " + post.get_user().getLasttName());
        userAvatart.setImageBitmap(post.get_user().getAvatar());
        DateTime postedAt = new DateTime(post.get_postedAt());
        posDate.setText(postedAt.toString("MMMM dd, yyyy HH:mm"));
        quickComment.setText(post.get_subject());
        if(post.get_image() != null){
            //postImage.setVisibility(View.GONE);
            postImage.setImageBitmap(post.get_image());
        }
        else{
            postImage.setImageBitmap(null);
        }

        postSubject.setText("This is test comment");
        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachButtonEvent(v);
            }
        });



    }
    private void attachButtonEvent(View v) {

        Button b = (Button)v.findViewById(R.id.post_layout_btn_comments);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddEventActivity.class);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public void addAll(@NonNull Collection<? extends DomainPost> collection) {
        //super.addAll(collection);
        _items.clear();
        _items.addAll(collection);
        notifyDataSetChanged();
    }
}
