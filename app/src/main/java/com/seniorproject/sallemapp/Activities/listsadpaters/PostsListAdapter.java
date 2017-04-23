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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.seniorproject.sallemapp.Activities.ShowPostActivity;
import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.helpers.MyHelper;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

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
            ViewHolder viewHolder;
        try {
            if(convertView == null){
                LayoutInflater inflater =
                        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.post_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.userAvatart = (ImageView) convertView.findViewById(R.id.postLayout_imgUserAvatar);
                viewHolder.posDateTextView = (TextView) convertView.findViewById(R.id.postLayout_lblPostDate);
                viewHolder.posterNameTextView = (TextView) convertView.findViewById(R.id.postLayout_lblUserName);
                viewHolder.postImage = (ImageView) convertView.findViewById(R.id.postLayout_imgPostImage);
                viewHolder.subjectTextView = (TextView) convertView.findViewById(R.id.postLayout_txtPosSubject);
                viewHolder.commenterNameTextView = (TextView)convertView.findViewById(R.id.commentLayout_lblUserName);
                viewHolder.commenterAvatar =(ImageView)convertView.findViewById(R.id.commentLayout_imgUserAvatar);
                viewHolder.commentTextView=(TextView)convertView.findViewById(R.id.commentLayout_lblComment);
                viewHolder.commentDateTextView = (TextView)convertView.findViewById(R.id.commentLayout_commentDate);
                viewHolder.commentsContainer =(LinearLayout)convertView.findViewById(R.id.postLayout_commentContainer);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            DomainPost post = getItem(position);
            //Bind the UI elements to entity
           bindPost(post, viewHolder);
        }
        catch (Exception e){
            //Log the exception so it can be reviewed.
            //Log.d("SALLEM APP", e.getStackTrace().toString());
            throw e;
        }
        return convertView;
    }

    private void bindPost(DomainPost post,ViewHolder holder) {
        holder.posterNameTextView.setText(post.get_user().getFirstName() + " " + post.get_user().getLasttName());
        holder.userAvatart.setImageBitmap(post.get_user().getAvatar());
        holder.posDateTextView.setText(MyHelper.formatDateString(post.get_postedAt()));
        holder.subjectTextView.setText(post.get_subject());
        if(post.get_image() != null){
            //postImage.setVisibility(View.GONE);
           holder.postImage.setImageBitmap(post.get_image());
        }
        else{
          holder.postImage.setImageBitmap(null);
        }
        if(post.get_comments()!= null && post.get_comments().size() > 0){
            holder.commenterNameTextView.setVisibility(View.VISIBLE);
            holder.commenterAvatar.setVisibility(View.VISIBLE);
            holder.commentDateTextView.setVisibility(View.VISIBLE);
            holder.commentTextView.setVisibility(View.VISIBLE);

            DomainComment comment = post.get_comments().get(0);
            holder.commenterNameTextView.setText(comment.get_user().getFirstName() + " " +comment.get_user().getLasttName());
            holder.commenterAvatar.setImageBitmap(comment.get_user().getAvatar());
            holder.commentDateTextView.setText(MyHelper.formatDateString(comment.get_commentedAt()));
            holder.commentTextView.setText(comment.get_subject());
        }
        else{
            holder.commenterNameTextView.setVisibility(View.GONE);
            holder.commenterAvatar.setVisibility(View.GONE);
            holder.commentDateTextView.setVisibility(View.GONE);
            holder.commentTextView.setVisibility(View.GONE);
        }
    }

    private static class ViewHolder{
        ImageView userAvatart ;
        TextView posDateTextView ;
        TextView posterNameTextView ;
        ImageView postImage;
        TextView subjectTextView ;
        TextView commenterNameTextView;
        ImageView commenterAvatar;
        TextView commentTextView;
        TextView commentDateTextView;
        LinearLayout commentsContainer;

    }


}
