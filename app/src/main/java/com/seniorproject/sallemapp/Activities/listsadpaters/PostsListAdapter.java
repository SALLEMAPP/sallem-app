package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.seniorproject.sallemapp.Activities.ShowPostActivity;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Centeral on 2/21/2017.
 */

public class PostsListAdapter  extends ArrayAdapter<DomainPost> {
    private ArrayList<DomainPost> _items;
    private Context _adpaterContext;
    private String mSelectedId;
    public Button mCommentsButton;

    public PostsListAdapter(Context context, ArrayList<DomainPost> items) {
        super(context, R.layout.post_layout, items);
        _adpaterContext = context;
        _items = items;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = convertView;

        try {
            final DomainPost post = _items.get(position);
            if(v == null){
                LayoutInflater vi =
                        (LayoutInflater) _adpaterContext.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE
                        );
                v = vi.inflate(R.layout.post_layout, null);
                mCommentsButton = (Button) v.findViewById(R.id.postLayout_btnComments);
                mCommentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("post ID", post.get_id());
                        Intent i = new Intent(v.getContext(), ShowPostActivity.class);
                        Log.e("Selected Id", mSelectedId);
                        i.putExtra("postId", post.get_id());
                        v.getContext().startActivity(i);
                    }
                });

            }
            //Bind the UI elements to entity
            mSelectedId = post.get_id();
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
        ImageView userAvatart = (ImageView) v.findViewById(R.id.postLayout_imgUserAvatar);
        TextView posDate = (TextView)
                v.findViewById(R.id.postLayout_lblPostDate);
        TextView poster = (TextView)
                v.findViewById(R.id.postLayout_lblUserName);
        TextView postSubject = (TextView)
                v.findViewById(R.id.postLayout_txtComment);
        ImageView postImage = (ImageView)
                v.findViewById(R.id.postLayout_imgPostImage);
        TextView quickComment = (TextView)
                v.findViewById(R.id.postLayout_txtPosSubject);

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




    }
    private void attachButtonEvent(View v) {

        Button b = (Button)v.findViewById(R.id.postLayout_btnComments);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("POSTS LIST ADAPTER", mSelectedId);
                Intent i = new Intent(v.getContext(), ShowPostActivity.class);
                i.putExtra("postId", mSelectedId);
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
