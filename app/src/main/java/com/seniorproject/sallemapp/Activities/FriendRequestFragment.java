package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.seniorproject.sallemapp.Activities.listsadpaters.FriendRequestsListAdapter;
import com.seniorproject.sallemapp.Activities.listsadpaters.SearchUsersListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainFriendship;
import com.seniorproject.sallemapp.entities.DomainUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendRequestFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String _title;
    private int _page;
    private ArrayList<DomainFriendship> mFriendsRequests;
    private FriendRequestsListAdapter mAdapter = null;
    private View mCurrentView;
    private Context mContext;
    private ListView mResultList;

    private OnFragmentInteractionListener mListener;

    public FriendRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment FriendRequestFragment.
     */
    public static FriendRequestFragment newInstance(int page, String title) {
        FriendRequestFragment fragment = new FriendRequestFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCurrentView = inflater.inflate(R.layout.fragment_friend_request, container, false);
         mContext = getActivity().getApplicationContext();
        return mCurrentView;
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
}
