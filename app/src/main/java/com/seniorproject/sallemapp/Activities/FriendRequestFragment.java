package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.Activities.listsadpaters.FriendRequestsListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainFriendship;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Friendship;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.AzureBlob;
import com.seniorproject.sallemapp.helpers.MyHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendRequestFragment extends ListFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String _title;
    private int _page;
    private ArrayList<DomainFriendship> mFriendsRequests;
    private FriendRequestsListAdapter mAdapter = null;
    private View mCurrentView;
    private Context mContext;

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
        loadFriendRequest();
        return mCurrentView;
    }

    private void loadFriendRequest() {
        String userId = DomainUser.CURRENT_USER.getId();
        LoadFriendsRequestAsync requestAsync = new LoadFriendsRequestAsync(userId);
        requestAsync.execute();
    }

    private void wireResultList(ArrayList<DomainFriendship> result){
        mFriendsRequests = result;
        mAdapter = new FriendRequestsListAdapter(mContext, mFriendsRequests);
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
    public class LoadFriendsRequestAsync extends AsyncTask<Void,Void,List<DomainFriendship>> {
        private final String mUserId;
        public LoadFriendsRequestAsync(String userId){
            mUserId = userId;
        }

        @Override
        protected List<DomainFriendship> doInBackground(Void... params) {
            List<DomainFriendship> result = new ArrayList<>();
            try {
                MobileServiceClient client = MyHelper.getAzureClient(mContext);
                MobileServiceTable<Friendship> friendsTable = client.getTable(Friendship.class);
                List<Friendship> requests = friendsTable.where().field("id")
                        .eq(mUserId).and().field("StatusId").eq(1).execute().get();
                MobileServiceTable<User> userTable = client.getTable(User.class);

                if (requests != null && requests.size() > 0) {
                    for(Friendship friendship: requests){
                       String friendId = friendship.getFriendId();
                    List<User> users = userTable.where().field("id").eq(friendId).execute().get();
                        User user = users.get(0);
                        if(user != null){
                            Bitmap avatar = null;

                            try {
                                //In case no avatar, just fail gracefully.
                                String title = user.getImageTitle() + ".jpg";
                                avatar = AzureBlob.getImage(mContext, title);
                            } catch (StorageException e) {
                                //e.printStackTrace();
                                //Log.e("SALLEM APP", "doInBackground: " + e.getCause().getMessage());

                            }
                            DomainUser domainUser = new DomainUser(
                                   user.getId(), user.getFirstName(), user.getLastName(),
                                    user.getPassword(), user.getEmail(), user.getJoinedAt(),
                                    user.getImageTitle(), user.getStatus(),
                                    avatar, 0, 0, false
                            );
                            DomainFriendship domainFriendship = new DomainFriendship(friendship, domainUser);
                            result.add(domainFriendship);
                        }



                }
            }
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("SALLEM APP", e.getCause().getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<DomainFriendship> result) {
            if(result != null && result.size() > 0){
                wireResultList((ArrayList<DomainFriendship>) result);
            }
        }
    }
}
