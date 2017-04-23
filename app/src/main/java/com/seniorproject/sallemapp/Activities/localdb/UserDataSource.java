package com.seniorproject.sallemapp.Activities.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;

import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.MyHelper;

/**
 * Created by abdul on 04-Apr-2017.
 */

public class UserDataSource {
    private SQLiteDatabase db;
    private SallemDbHelper dbHelper;
    public UserDataSource(Context context){
        dbHelper = new SallemDbHelper(context);
    }
    public void open()throws SQLiteException{
        db= dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }
    public boolean insert(User user, Bitmap avatar){
        boolean ok = false;
        try {
            ContentValues values = new ContentValues();
            values.put("Id", user.getId());
            values.put("FirstName", user.getFirstName());
            values.put("LastName", user.getLastName());
            values.put("Email", user.getEmail());
            values.put("Password", user.getPassword());
            values.put("JoinedAt", user.getJoinedAt());
            values.put("ImageTitle", user.getImageTitle());
            values.put("StatusId", user.getStatus());
            values.put("Avatar", MyHelper.encodeBitmap(avatar));
            ok =db.insert("user",null, values) > 0;

        }
        catch (Exception e){

        }
        return ok;
    }
    public DomainUser getUser(String userId){
        String q = "SELECT * FROM user WHERE Id = " +"'" +userId + "'";
        Cursor cursor = db.rawQuery( q, null);
        if( cursor.moveToFirst()){
            String id = cursor.getString(0);
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String email = cursor.getString(3);
            String passowrd = cursor.getString(4);
            String joinedAt = cursor.getString(5);
            String imageTitle = cursor.getString(6);
            int statusId = cursor.getInt(7);
            byte[] blobData = cursor.getBlob(8);
            Bitmap avatar = MyHelper.decodeImage(blobData);
            DomainUser user = new DomainUser(id, firstName, lastName, passowrd, email, joinedAt, imageTitle, statusId, avatar, 0, 0, false);
            return user;
        }
        return null;
    }
    public void update(String id, int status, Bitmap avatar){
        ContentValues updateValue = new ContentValues();
        updateValue.put("StatusId", status);
        updateValue.put("Avatar", MyHelper.encodeBitmap(avatar));
        db.update("user", updateValue, "id = " + "'" + id + "'", null );
    }
}
