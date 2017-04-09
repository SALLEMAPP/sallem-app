package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.seniorproject.sallemapp.Activities.ShowPostActivity;
import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Centeral on 2/21/2017.
 */

public class PostsListAdapter  extends ArrayAdapter<DomainPost> {
    private ArrayList<DomainPost> _items;
    private Context mContext;
    public PostsListAdapter(Context context, ArrayList<DomainPost> items) {
       super(context, R.layout.post_layout, items);
        mContext = context;
        _items = items;

    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public DomainPost getItem(int position) {
        return _items.get(position);
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = convertView;

        try {
            final DomainPost post = _items.get(position);
            if(v == null){
                LayoutInflater vi =
                        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ImageView userAvatart = (ImageView) v.findViewById(R.id.postLayout_imgUserAvatar);
        TextView posDate = (TextView)
                v.findViewById(R.id.postLayout_lblPostDate);
        TextView poster = (TextView)
                v.findViewById(R.id.postLayout_lblUserName);

        ImageView postImage = (ImageView)
                v.findViewById(R.id.postLayout_imgPostImage);
        TextView subject = (TextView)
                v.findViewById(R.id.postLayout_txtPosSubject);
        ListView commentsList = (ListView) v.findViewById(R.id.postLayout_commentsList);

        poster.setText(post.get_user().getFirstName() + " " + post.get_user().getLasttName());
        userAvatart.setImageBitmap(post.get_user().getAvatar());
        DateTime postedAt = new DateTime(post.get_postedAt());
        posDate.setText(postedAt.toString("MMMM dd, yyyy HH:mm"));
        subject.setText(post.get_subject());
        if(post.get_image() != null){
            //postImage.setVisibility(View.GONE);
            postImage.setImageBitmap(post.get_image());
        }
        else{
            postImage.setImageBitmap(null);
        }
        List<DomainComment> postCommments = post.get_comments();
        if(postCommments != null && postCommments.size() > 0){
            ArrayList<DomainComment> topComments = new ArrayList<>();
            for(int i = 0; i < postCommments.size(); i++){
                if(i > 1){break;}
                topComments.add(postCommments.get(i));

                CommentsListAdapter commentsAdapter = new CommentsListAdapter(mContext, topComments);
                commentsList.setAdapter(commentsAdapter);
            }
        }
    }


}
