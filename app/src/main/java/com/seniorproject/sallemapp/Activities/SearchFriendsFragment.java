package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.seniorproject.sallemapp.Activities.listsadpaters.SearchUsersListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.helpers.ListAsyncResult;
import com.seniorproject.sallemapp.helpers.MyApplication;
import com.seniorproject.sallemapp.helpers.SearchUsersAsync;

import java.util.ArrayList;
import java.util.List;


public class SearchFriendsFragment extends Fragment implements ListAsyncResult<DomainUser> {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String _title;
    private int _page;
    private ArrayList<DomainUser> mUsers;
    private SearchUsersListAdapter mAdapter = null;
    Button mSearchButton;

    private OnFragmentInteractionListener mListener;
    private View mCurrentView;
    private Context mContext;
    private ListView mResultList;
    private EditText mEmailText;
    public SearchFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment SearchFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFriendsFragment newInstance(int page, String title) {
        SearchFriendsFragment fragment = new SearchFriendsFragment();
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
        mCurrentView =  inflater.inflate(R.layout.fragment_search_friends, container, false);
        mContext = getActivity().getApplicationContext();
        mResultList = (ListView) mCurrentView.findViewById(R.id.searchFriends_listUsers);
        mEmailText = (EditText) mCurrentView.findViewById(R.id.searchFriends_txtEmail);
        mSearchButton =  (Button)mCurrentView.findViewById(R.id.searchFriends_btnSearch);
        wireResultList(new ArrayList<DomainUser>());
        wireSearchButton();

        return mCurrentView;
    }
    private void wireResultList(ArrayList<DomainUser> result){
        mUsers = result;
        mAdapter = new SearchUsersListAdapter(mContext, mUsers);
        mResultList.setAdapter(mAdapter);
    }
    private void wireSearchButton() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailText.getText().toString();
                if(email.isEmpty()){return;}
                SearchUsersAsync search = new SearchUsersAsync
                        (email, mContext, SearchFriendsFragment.this);
                search.execute();


            }
        });
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
    public void processFinish(List<DomainUser> result) {
        wireResultList((ArrayList<DomainUser>)result);
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
