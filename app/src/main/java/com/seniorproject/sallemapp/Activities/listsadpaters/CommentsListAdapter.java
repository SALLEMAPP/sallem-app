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
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainPost;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by abdul on 12-Mar-2017.
 */

public class CommentsListAdapter extends ArrayAdapter<DomainComment> {
    private ArrayList<DomainComment> _items;
    private Context _adpaterContext;
    private String mSelectedId;
    public CommentsListAdapter(Context context, ArrayList<DomainComment> items) {
        super(context, R.layout.comments_layout, items);
        _adpaterContext = context;
        _items = items;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        try {
            DomainComment comment = _items.get(position);
            if(v == null){
                LayoutInflater vi =
                        (LayoutInflater) _adpaterContext.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE
                        );
                v = vi.inflate(R.layout.comments_layout, null);
            }
            //Bind the UI elements to entity
            bindComment(comment, v);

        }
        catch (Exception e){
            //Log the exception so it can be reviewed.
            //Log.d("SALLEM APP", e.getStackTrace().toString());
            throw e;
        }
        return v;
    }

    private void bindComment(DomainComment comment, View v) {
        ImageView userAvatart = (ImageView) v.findViewById(R.id.commentLayout_imgUserAvatar);
        TextView commentDate = (TextView)
                v.findViewById(R.id.commentLayout_postDate);
        TextView commenter = (TextView)
                v.findViewById(R.id.commentLayout_lblUserName);
        TextView commentSubject = (TextView)
                v.findViewById(R.id.commentLayout_lblComment);

        commenter.setText(comment.get_user().getFirstName() + " " + comment.get_user().getLasttName());
        userAvatart.setImageBitmap(comment.get_user().getAvatar());
        DateTime postedAt = new DateTime(comment.get_commentedAt());
        commentDate.setText(postedAt.toString("MMMM dd, yyyy HH:mm"));




    }

    @Override
    public void addAll(@NonNull Collection<? extends DomainComment> collection) {
        //super.addAll(collection);
        _items.clear();
        _items.addAll(collection);
        notifyDataSetChanged();
    }
}
