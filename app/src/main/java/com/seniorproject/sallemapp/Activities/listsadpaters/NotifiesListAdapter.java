package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.Notify;
import com.seniorproject.sallemapp.helpers.MyHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by abdul on 19-Apr-2017.
 */

public class NotifiesListAdapter extends ArrayAdapter<Notify> {

    private Context mContext;
    private ArrayList<Notify> mItems;
    public NotifiesListAdapter(@NonNull Context context, ArrayList<Notify> items) {
        super(context, R.layout.notifies_list);
        mContext = context;
        mItems = items;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notifies_list, null);
            holder = new ViewHolder();
            holder.msgTextView = (TextView) convertView.findViewById(R.id.notify_lblMsg);
            holder.dateTextView=(TextView) convertView.findViewById(R.id.notify_lblDate);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        Notify notify = getItem(position);
        bindNotify(notify, holder);
        return convertView;

    }

    private void bindNotify(Notify notify, ViewHolder holder) {
        String msg  = notify.getTitle() +
                " "+ notify.getSubject();
        holder.msgTextView.setText(msg);
        String publishedAt = MyHelper.formatDateString(notify.getPublishedAt());
        holder.dateTextView.setText(publishedAt);
    }

    private static class ViewHolder{
        TextView msgTextView;
        TextView dateTextView;

    }

}
