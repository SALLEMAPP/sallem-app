package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
            //ImageView friendAvatar = (ImageView)
                    //v.findViewById(R.id.img_friend_avatar);
            //friendAvatar.setImageBitmap();
            TextView friendName = (TextView)
                    v.findViewById(R.id.lbl_friend_name);
            String name = user.getLasttName() + " " + user.getFirstName();
            friendName.setText(name);
        }
        catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }
//    public void showDelete(final int postion, final View convertView, final Context context, final Contact contact){
//        View v = convertView;
//        final Button b = (Button) v.findViewById(R.id.buttonDeleteContact);
//        if(b.getVisibility() == View.INVISIBLE){
//            b.setVisibility(View.VISIBLE);
//            b.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    hideDelete(postion, convertView, context);
//                    items.remove(contact);
//                    deleteOption(contact.getContactID(), context);
//                }
//            });
//        }
//        else {
//            hideDelete(postion, convertView, context);
//        }
//
//    }
//    private void deleteOption(int contactToDelete, Context context){
//        ContactDataSource db = new ContactDataSource(context);
//        db.open();
//        db.deleteContact(contactToDelete);
//        db.close();
//        this.notifyDataSetChanged();
//    }
//    private void hideDelete(int contactToDelete, View convertView, Context context){
//        View v = convertView;
//        final Button b = (Button)v.findViewById(R.id.buttonDeleteContact);
//        b.setVisibility(View.INVISIBLE);
//        b.setOnClickListener(null);
//    }
}
