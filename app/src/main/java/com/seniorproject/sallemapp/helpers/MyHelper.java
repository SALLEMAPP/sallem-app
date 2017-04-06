package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.seniorproject.sallemapp.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Created by abdul on 31-Mar-2017.
 */

public class MyHelper {

    public static final String DEFAULT_AVATAR_TITLE= "3d788b86-4313-40aa-8a4f-172344ed139d";
    public static final String SHARED_PREFERENCE_NAME = "sallemappsettings";
    public static Bitmap getDefaultAvatar(Context context){
        Bitmap avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_user_avatar);
        return avatar;

    }
    public static byte[] encodeBitmap(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;

    }
    public static Bitmap decodeImage(byte[] stream){

        Bitmap bm = BitmapFactory.decodeByteArray(stream, 0, stream.length);
        return bm;

    }
}
