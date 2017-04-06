package com.seniorproject.sallemapp.Activities.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abdul on 04-Apr-2017.
 */

public class SallemDbHelper extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "sallem.db";
   private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_USER =
            "create table user " +
                    "(Id text primary key, " +
                    "FirstName text not null, " +
                    "LastName text not null, " +
                    "Email text not null, " +
                    "Password text not null, " +
                    "JoinedAt text not null, " +
                    "ImageTitle text not null, " +
                    "StatusId integer not null, " +
                    "Avatar blob not null)";
    private static final String CREATE_TABLE_POST =
            "create table post " +
                    "(Id text primary key, " +
                    "PostedAt text not null, " +
                    "Subject text not null, " +
                    "UserId text not null, " +
                    "ImagePath text null, " +
                    "ActivityId text null, " +
                    "UpdatedAt text not null)";
    private static final String CREATE_TABLE_COMMENT =
            "create table comment " +
                    "(Id text primary key, " +
                    "CommentedAt text not null, " +
                    "UserId text not null, " +
                    "Subject text not null, " +
                    "PostId text null, " +
                    " UpdatedAt text not null)";

    public SallemDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_POST);
        db.execSQL(CREATE_TABLE_COMMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);

    }
}
