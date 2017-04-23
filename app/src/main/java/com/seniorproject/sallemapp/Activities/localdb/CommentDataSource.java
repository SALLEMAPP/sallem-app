package com.seniorproject.sallemapp.Activities.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.seniorproject.sallemapp.entities.Comment;
import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.helpers.MyHelper;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 05-Apr-2017.
 */

public class CommentDataSource {
    private SQLiteDatabase db;
    private SallemDbHelper dbHelper;
    private Context mContext;
    public CommentDataSource(Context context){
        dbHelper = new SallemDbHelper(context);
        mContext = context;
    }
    public void open()throws SQLiteException {
        db= dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }
    public boolean insert(Comment comment){
        boolean ok = false;
        try {
            ContentValues values = new ContentValues();
            values.put("Id", comment.get_id());
            values.put("CommentedAt", comment.get_commentedAt());
            values.put("UserId" , comment.get_userId());
            values.put("Subject", comment.get_subject());
            values.put("PostId", comment.get_postId());
            values.put("UpdatedAt", MyHelper.getCurrentDateTime());
            ok =db.insert("comment",null, values) > 0;

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
        String q = "SELECT MAX(UpdatedAt) from comment";
        Cursor cursor  = db.rawQuery(q, null);
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                max = cursor.getString(0);
            }
        }
        return max;
    }
    public ArrayList<DomainComment> getComments(String postId){
        ArrayList<DomainComment> postComments = new ArrayList<>();
        String q = "SELECT * FROM user WHERE PostId = " +"'" +postId + "'";
        Cursor cursor = db.rawQuery( q, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                String commentedAt = cursor.getString(1);
                String userId = cursor.getString(2);
                String subject = cursor.getString(3);
                String dbPostId = cursor.getString(4);
                DomainComment comment = new DomainComment();
                comment.set_id(id);
                comment.set_commentedAt(commentedAt);
                comment.set_subject(subject);
                comment.set_posId(dbPostId);
                UserDataSource userdc = new UserDataSource(mContext);
                userdc.open();
                DomainUser user = userdc.getUser(userId);
                comment.set_id(userId);
                comment.set_user(user);
                userdc.close();
                postComments.add(comment);

            }
        }

        return postComments;
    }
}
