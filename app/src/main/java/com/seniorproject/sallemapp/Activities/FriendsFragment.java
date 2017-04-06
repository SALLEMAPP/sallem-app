package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.seniorproject.sallemapp.Activities.listsadpaters.FriendsListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String _title;
    private int _page;
    private FriendsListAdapter _adpater= null;
    private View _currentView;
    ArrayList<DomainUser> _friendsList = new ArrayList<DomainUser>();

    private OnFragmentInteractionListener mListener;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(int page, String title) {
        FriendsFragment fragment = new FriendsFragment();
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
        _currentView =    inflater.inflate(R.layout.fragment_friends, container, false);
        attachList();
        attachSearchButton();
        return _currentView;

    }

    private void attachSearchButton() {
        Button b = (Button) _currentView.findViewById(R.id.btn_search_friends);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText)_currentView.findViewById(R.id.txt_search_friends);
                String searchTerm = searchText.getText().toString();
                searchFriends(searchTerm);
            }
        });

    }
    private void searchFriends(final String term){
    ArrayList<DomainUser> s = new ArrayList<>();
        for(int i = 0; i < _friendsList.size(); i++){
        boolean b = _friendsList.get(i).getLasttName().toLowerCase().contains(term);
        boolean n = _friendsList.get(i).getFirstName().toLowerCase().contains(term);
            if(b || n){
                s.add(_friendsList.get(i));
            }
    }
        _adpater = new FriendsListAdapter(this.getContext(), s);
        setListAdapter(_adpater);
    }

    private void attachList() {
        _friendsList = dummyData();
        _adpater = new FriendsListAdapter(this.getContext(), _friendsList);
        setListAdapter(_adpater);


    }
    private ArrayList<DomainUser> dummyData(){
        ArrayList r = new ArrayList<User>();
//        DomainUser s = new DomainUser();
//        s.setId(UUID.randomUUID().toString());
//        s.setFirstName("Amr");
//        s.setLastName("Zaid");
//        r.add(s);s = new User();
//        s.setId(UUID.randomUUID().toString());
//        s.setFirstName("Ali");
//        s.setLastName("Ahmed");
//        r.add(s);
//        s = new User();
//        s.setId(UUID.randomUUID().toString());
//        s.setFirstName("Khalid");
//        s.setLastName("Omar");
//        r.add(s);
//        s = new User();
//        s.setId(UUID.randomUUID().toString());
//        s.setFirstName("Saeed");
//        s.setLastName("Saleh");
//        r.add(s);
//        s = new User();
//
//        s.setId(UUID.randomUUID().toString());
//        s.setFirstName("Muhammad");
//        s.setLastName("Yousf");
//        r.add(s);
        //Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_me_24px);
        //s.setAvatar(b);


        return r;

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
