package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.seniorproject.sallemapp.R;

/**
 * Created by abdul on 31-Mar-2017.
 */

public class MyHelper {
    public static Bitmap getDefaultAvatar(Context context){
        Bitmap avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_black_24dp);
        return avatar;

    }
}
