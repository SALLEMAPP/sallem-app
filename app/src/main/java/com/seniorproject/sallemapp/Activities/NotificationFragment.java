package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.seniorproject.sallemapp.Activities.listsadpaters.NotifiesListAdapter;
import com.seniorproject.sallemapp.Activities.localdb.NotifyDataSource;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Notify;
import com.seniorproject.sallemapp.helpers.LoadNotifiesAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends ListFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mPage;
    private String mTitle;
    private ArrayList<Notify> mNotifiesList;
    private NotifiesListAdapter mAdapter;
    private Context mContext;
    private Button mMarkAllButton;



    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotifies();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(int page, String title) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PARAM1);
            mTitle = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_notification, container, false);
        mContext = getContext();
        attachNotifiesList(v);
        attachMarkAllButton(v);

        return v;
    }

    private void attachMarkAllButton(View v) {
        mMarkAllButton = (Button) v.findViewById(R.id.notify_btnMarkRead);
        mMarkAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyDataSource notifyDataSource = new NotifyDataSource(mContext);
                notifyDataSource.open();
                for (Notify notify:mNotifiesList){
                    notifyDataSource.markAsRead(notify.getId());
                }
                notifyDataSource.close();
            }
        });
    }

    private void attachNotifiesList(View v) {
        mNotifiesList = new ArrayList<>();
        mAdapter = new NotifiesListAdapter(mContext, mNotifiesList);
        setListAdapter(mAdapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void loadNotifies(){
        LocalNotifiesAsync localNotifies = new LocalNotifiesAsync();
        localNotifies.execute();

    }
    private void refreshList(List<Notify> notifies){
        if(notifies != null) {
            mNotifiesList.clear();
            mNotifiesList = (ArrayList<Notify>) notifies;
            mAdapter.addAll(mNotifiesList);
            mAdapter.notifyDataSetChanged();
        }
    }
    private class LocalNotifiesAsync extends AsyncTask<Void, Void, List<Notify>>{

        @Override
        protected List<Notify> doInBackground(Void... params) {
            List<Notify> notifies = null;
            try {
                NotifyDataSource notifyDataSource = new NotifyDataSource(mContext);
                notifyDataSource.open();
                notifies = notifyDataSource.getNonReadNotifies(DomainUser.CURRENT_USER.getId());
                notifyDataSource.close();

            }
            catch (Exception e){
                Log.e("Load Notify Async", "doInBackground: " +e.getMessage() );
            }
            return notifies;
        }

        @Override
        protected void onPostExecute(List<Notify> notifies) {
            refreshList(notifies);
        }
    }
}
