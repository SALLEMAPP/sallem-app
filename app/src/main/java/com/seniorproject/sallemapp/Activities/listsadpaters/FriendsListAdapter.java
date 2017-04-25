package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;

import java.util.ArrayList;

/**
 * Created by abdul on 19-Feb-2017.
 */

public class FriendsListAdapter extends ArrayAdapter<DomainUser> {
    private ArrayList<DomainUser> _items;
    private Context _adpaterContext;
    public FriendsListAdapter(Context context, ArrayList<DomainUser> items) {
        super(context, R.layout.friends_list, items);
        _adpaterContext = context;
        _items = items;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        try {
            DomainUser user = _items.get(position);
            if(v == null){
                LayoutInflater vi =
                        (LayoutInflater) _adpaterContext.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE
                        );
                v = vi.inflate(R.layout.friends_list, null);
            }
            ImageView friendAvatar = (ImageView)
                    v.findViewById(R.id.img_friend_avatar);
            Bitmap scaledImage= Bitmap.createScaledBitmap(user.getAvatar(), 185, 185, false);
            friendAvatar.setImageBitmap(scaledImage);
            TextView friendName = (TextView)
                    v.findViewById(R.id.lbl_friend_name);
            String name = user.getFirstName() + " " + user.getLasttName();
            friendName.setText(name);
        }
        catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }

}
