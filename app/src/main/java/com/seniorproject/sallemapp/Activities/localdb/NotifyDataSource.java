package com.seniorproject.sallemapp.Activities.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;

import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Notify;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.MyHelper;

import java.util.ArrayList;

/**
 * Created by abdul on 19-Apr-2017.
 */

public class NotifyDataSource {
    private SQLiteDatabase db;
    private SallemDbHelper dbHelper;
    public NotifyDataSource(Context context){
        dbHelper = new SallemDbHelper(context);
    }
    public void open()throws SQLiteException {
        db= dbHelper.getWritableDatabase();
        //db.execSQL("Delect from notify");
    }

    public void close(){
        dbHelper.close();
    }
    public boolean insert(Notify notify){
        boolean ok = false;
        try {
            ContentValues values = new ContentValues();
            values.put("Id", notify.getId());
            values.put("sourceUser", notify.getSourceUser());
            values.put("destUser", notify.getDestUser());
            values.put("publishedAt", notify.getPublishedAt());
            values.put("title", notify.getTitle());
            values.put("subject", notify.getSubject());
            values.put("read", 0);
            ok =db.insert("notify",null, values) > 0;

        }
        catch (Exception e){

        }
        return ok;
    }

    public ArrayList<Notify> getNonReadNotifies(String userId){
        String q = "SELECT * FROM notify WHERE read = 0 and destUser = " + "'" + userId + "'" + " ORDER BY publishedAt DESC";

        Cursor cursor = db.rawQuery( q, null);
        ArrayList<Notify> notifes = new ArrayList<>();
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String sourceUser = cursor.getString(1);
                String distUser = cursor.getString(2);
                String publishedAt = cursor.getString(3);
                String title = cursor.getString(4);
                String subject = cursor.getString(5);

                Notify notify = new Notify();
                notify.setId(id);
                notify.setSourceUser(sourceUser);
                notify.setDestUser(distUser);
                notify.setPublishedAt(publishedAt);
                notify.setTitle(title);
                notify.setSubject(subject);
                notifes.add(notify);
            }


        return notifes;
    }
    public void markAsRead(String id){
        ContentValues updateValue = new ContentValues();
        updateValue.put("read", 1);
        db.update("notify", updateValue, "id = " + "'" + id + "'", null );
    }
}
