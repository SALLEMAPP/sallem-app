package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.Activities.listsadpaters.SearchUsersListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.AzureHelper;
import com.seniorproject.sallemapp.helpers.DownloadImage;
import com.seniorproject.sallemapp.helpers.MyHelper;

import java.util.ArrayList;
import java.util.List;


public class SearchFriendsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String _title;
    private int _page;
    private ArrayList<DomainUser> mUsers;
    private SearchUsersListAdapter mAdapter = null;
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
        Button searchButton =  (Button)mCurrentView.findViewById(R.id.searchFriends_btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailText.getText().toString();
                if(email.isEmpty()){return;}
                SearchUsersAsync search = new SearchUsersAsync(email);
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
    public class SearchUsersAsync extends AsyncTask<Void,Void,List<DomainUser>>{
        private final String mEmail;
        public SearchUsersAsync(String email){
            mEmail = email;
        }

        @Override
        protected List<DomainUser> doInBackground(Void... params) {
            List<DomainUser> resultUsers = new ArrayList<>();
            try {
                MobileServiceClient client = AzureHelper.CreateClient(mContext);
                MobileServiceTable<User> userTable = client.getTable(User.class);
                List<User> users = userTable.where().startsWith("email", mEmail).execute().get();
                if(users.size() > 0){
                    for(User user :users) {
                        Bitmap avatar = null;

                        try{
                            //In case no avatar, just fail gracefully.
                          String title = user.getImageTitle() +".jpg";
                          avatar = DownloadImage.getImage(mContext, title);
                        }
                        catch(StorageException e){
                            //e.printStackTrace();
                            //Log.e("SALLEM APP", "doInBackground: " + e.getCause().getMessage());

                        }
                        DomainUser domainUser = new DomainUser(
                                user.getId(), user.getFirstName(), user.getLastName(),
                                user.getPassword(), user.getEmail(), user.getJoinedAt(),
                                user.getImageTitle(), user.getStatus(),
                                avatar, 0, 0, false
                        );
                        resultUsers.add(domainUser)    ;
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("SALLEM APP", e.getCause().getMessage());
            }
            return resultUsers;
        }

        @Override
        protected void onPostExecute(List<DomainUser> result) {
              if(result != null && result.size() > 0){
                wireResultList((ArrayList<DomainUser>) result);
            }
        }
    }
}
