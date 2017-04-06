package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainFriendship;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Friendship;
import com.seniorproject.sallemapp.helpers.PassObject;
import com.seniorproject.sallemapp.helpers.UpdateFriendRequestAsync;

import java.util.ArrayList;

/**
 * Created by abdul on 03-Apr-2017.
 */

public class SelectUsersListAdapter extends ArrayAdapter<DomainUser>  {
    private ArrayList<DomainUser> mItems;
    private Context mAdpaterContext;
    public CheckBox mSelectedCheckBox;
    public Button mDeclineButton;
    PassObject<DomainUser> mParent;

    public SelectUsersListAdapter(@NonNull Context context, ArrayList<DomainUser> items, PassObject<DomainUser> parent) {
        super(context, R.layout.participants_list, items);
        mAdpaterContext = context;
        mItems = items;
        mParent= parent;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        try {
            final DomainUser user = mItems.get(position);
            if(v == null){
                LayoutInflater vi =
                        (LayoutInflater) mAdpaterContext.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE
                        );
                v = vi.inflate(R.layout.participants_list, null);
                mSelectedCheckBox = (CheckBox) v.findViewById(R.id.paricipationList_ckbSelect);
               mSelectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       if(isChecked){
                            user.setIselected(true);
                           onPassed(user);
                       }
                       else{
                           user.setIselected(false);
                           onPassed(user);

                       }
                   }
               });

            }
            //Bind the UI elements to entity
            bindUser(user, v);

        }
        catch (Exception e){
            //Log the exception so it can be reviewed.
            //Log.d("SALLEM APP", e.getStackTrace().toString());
            throw e;
        }
        return v;
    }
    private void bindUser(DomainUser user, View v){
        ImageView avatar = (ImageView) v.findViewById(R.id.paricipationList_imgAvatar);
        TextView userName = (TextView) v.findViewById(R.id.paricipationList_lblUserName);

        if(user.getAvatar() == null){
            avatar.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }
        else{
            avatar.setImageBitmap(user.getAvatar());
        }
        userName.setText(user.getFirstName() + " "
                + user.getLasttName());
    }

    public void onPassed(DomainUser obj) {
        if(mParent!= null){
            mParent.onPassed(obj);
        }
    }
}
