package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.seniorproject.sallemapp.Activities.dbhelpers.DbContext;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Centeral on 2/21/2017.
 */

public class PostsListAdapter  extends ArrayAdapter<Post> {
    private ArrayList<Post> _items;
    private Context _adpaterContext;
    public PostsListAdapter(Context context, ArrayList<Post> items) {
        super(context, R.layout.post_layout, items);
        _adpaterContext = context;
        _items = items;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        try {
            Post post = _items.get(position);
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
            Log.d("SALLEM APP", e.getStackTrace().toString());
        }
        return v;
    }

    private void bindPost(Post post, View v) {
        DbContext db = new DbContext();
        TextView userName = (TextView)
                v.findViewById(R.id.post_layout_lbl_user_name);
        TextView postDate = (TextView)
                v.findViewById(R.id.post_layout_lbl_post_date);
        EditText postSubject = (EditText)
                v.findViewById(R.id.post_layout_txt_post_subject);
        ImageView postImage = (ImageView)
                v.findViewById(R.id.post_layout_img_post_image);
        Button commentsButton = (Button)
                v.findViewById(R.id.post_layout_btn_comments);
        Button quickCommentButton = (Button)
                v.findViewById(R.id.post_layout_btn_add_comment);
        EditText quickComment = (EditText)
                v.findViewById(R.id.post_layout_txt_quich_comment);
        userName.setText((db.getUser(post.getPostId()).getLastName()));
        LocalDateTime now = new LocalDateTime();

        postDate.setText(now.toDateTime().toString());



    }
}
