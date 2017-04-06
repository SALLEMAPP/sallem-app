package com.seniorproject.sallemapp.Activities.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;

import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.MyHelper;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;

/**
 * Created by abdul on 05-Apr-2017.
 */

public class PostDataSource {
    private SQLiteDatabase db;
    private SallemDbHelper dbHelper;
    private Context mContext;
    public PostDataSource(Context context){
        dbHelper = new SallemDbHelper(context);
        mContext = context;
    }
    public void open()throws SQLiteException {
        db= dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }
    public boolean insert(Post post){
        boolean ok = false;
        try {
            ContentValues values = new ContentValues();
            values.put("Id", post.getId());
            values.put("PostedAt", post.getPostedAt());
            values.put("Subject" , post.getSubject());
            values.put("UserId", post.getUserId());
            values.put("ImagePath", post.get_imagePath());
            values.put("ActivityId", post.getActivityId());
            values.put("UpdatedAt", new LocalDateTime().toString());
            ok =db.insert("post",null, values) > 0;

        }
        catch (Exception e){

        }
        return ok;
    }
    /*
    Get last update date.
    @return the last update or null.
     */
    public String getLastUpdate(){
        String max = null;
        String q = "SELECT MAX(UpdatedAt) from post";
        Cursor cursor  = db.rawQuery(q, null);
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                max = cursor.getString(0);
            }
        }
        return max;
    }
    public DomainPost getPost(String postId){
        String q = "SELECT * FROM user WHERE Id = " +"'" +postId + "'";
        Cursor cursor = db.rawQuery( q, null);
        if( cursor.moveToFirst()){
            DomainPost post = createPost(cursor);
            return post;
        }
        return null;
    }
    public ArrayList<DomainPost> getAll(){
        ArrayList<DomainPost> posts = new ArrayList<>();
        String q = "SELECT * FROM post";
        Cursor cursor = db.rawQuery(q, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                DomainPost post = createPost(cursor);
                posts.add(post);
            }
        }
        return posts;
    }
    public ArrayList<DomainPost> getMyPosts(String id){
        ArrayList<DomainPost> posts = new ArrayList<>();
        String q = "SELECT * FROM post WHERE id = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(q, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                DomainPost post = createPost(cursor);
                posts.add(post);
            }
        }
        return posts;
    }
    private DomainPost createPost(Cursor cursor){
        String id = cursor.getString(0);
        String postedAt = cursor.getString(1);
        String subject = cursor.getString(2);
        String userId = cursor.getString(3);
        String imagePath = cursor.getString(4);
        String activtiyId = cursor.getString(5);
        //String updateAt = cursor.getString(6);
        DomainPost post = new DomainPost();
        post.set_id(id);
        post.set_postedAt(postedAt);
        post.set_subject(subject);
        UserDataSource userdc = new UserDataSource(mContext);
        userdc.open();
        DomainUser user = userdc.getUser(userId);
        post.set_userId(userId);
        post.set_user(user);
        userdc.close();
        post.setImagePath(imagePath);
        post.set_activityId(activtiyId);
        CommentDataSource commentdc = new CommentDataSource(mContext);
        commentdc.open();
        ArrayList<DomainComment> comments = commentdc.getComments(post.get_id());
        post.set_comments(comments);
        return post;
    }
}
