package com.seniorproject.sallemapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.seniorproject.sallemapp.Activities.listsadpaters.PostsListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.helpers.CommonMethods;
import com.seniorproject.sallemapp.helpers.ListAsyncResult;
import com.seniorproject.sallemapp.helpers.MyApplication;
import com.seniorproject.sallemapp.helpers.MyHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends ListFragment implements ListAsyncResult<DomainPost>
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PostsListAdapter _adpater= null;
    private ArrayList<DomainPost> mPostsList;
    private View _currentView;
    private int _page;
    private String _title;

    private OnFragmentInteractionListener mListener;
    private ProgressBar mLoadinggProgressBar;
    private  static Context mContext;
    EventsReceiver mEventsReciever;
    IntentFilter mIntentFilter;
    IntentFilter mAddNewPostFiler;
    EventsReceiver mAddNewPostReceiver;
    private MyApplication mMyApp;
    public PostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment PostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostsFragment newInstance(int page, String title) {
        PostsFragment fragment = new PostsFragment();
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
            _page = getArguments().getInt(ARG_PARAM1);
            _title = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(mEventsReciever);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _currentView  = inflater.inflate(R.layout.fragment_posts, container, false);
        mLoadinggProgressBar = (ProgressBar) _currentView.findViewById(R.id.postFrag_progressBar);
        mLoadinggProgressBar.setVisibility(View.VISIBLE);
        mContext = getActivity().getApplicationContext();
        mPostsList = new ArrayList<>();
        _adpater = new PostsListAdapter(mContext, mPostsList);
        setListAdapter(_adpater);

        mEventsReciever = new EventsReceiver();
        mIntentFilter = new IntentFilter(CommonMethods.ACTION_NOTIFY_REFRESH);
        mAddNewPostReceiver = new EventsReceiver();
        mAddNewPostFiler = new IntentFilter(CommonMethods.ACTION_NOTIFY_ADD_POST);

        getActivity().registerReceiver(mEventsReciever, mIntentFilter);
        getActivity().registerReceiver(mAddNewPostReceiver, mAddNewPostFiler);
        mMyApp =(MyApplication) getActivity().getApplication();

        return _currentView;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
       DomainPost post =  _adpater.getItem(position);
        Intent i = new Intent(v.getContext(), ShowPostActivity.class);
        i.putExtra("postId", post.get_id());
        v.getContext().startActivity(i);
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

    @Override
    public void processFinish(List<DomainPost> result) {
        if(result != null) {
            _adpater.addAll(result);
            _adpater.notifyDataSetChanged();

            mLoadinggProgressBar.setVisibility(View.GONE);
        }

   }
   private void addNewPost(DomainPost post){
       _adpater.insert(post, 0);
       _adpater.notifyDataSetChanged();


   }


    @Override
    public void onResume() {
        super.onResume();
        if(mMyApp.Posts_Cach != null){
            processFinish(mMyApp.Posts_Cach);
        }

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
    public class EventsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                if(intent.getAction() == CommonMethods.ACTION_NOTIFY_REFRESH){
                    List<DomainPost> posts = mMyApp.Posts_Cach;
                    processFinish(posts);
                    return;
                }
                if(intent.getAction() == CommonMethods.ACTION_NOTIFY_ADD_POST){
                    DomainPost post = intent.getExtras().getParcelable("newPost");
                    Bitmap image = null;
                    if(post != null){
                        if(post.getImagePath() != null) {
                            image = MyHelper.readImageFromDisk(getActivity().getApplicationContext(), post.getImagePath());
                        }
                        if(image != null){
                            post.set_image(image);
                        }

                        addNewPost(post);
                    }
                    return;
                }
            }
        }
    }
}

