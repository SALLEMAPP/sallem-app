package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.seniorproject.sallemapp.Activities.listsadpaters.PostsListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.Comment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.PostImage;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.CommonMethods;
import com.seniorproject.sallemapp.helpers.DownloadImage;
import com.seniorproject.sallemapp.helpers.ListAsyncResult;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends ListFragment implements ListAsyncResult<DomainPost> {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Post> _friendsList = new ArrayList<Post>();
    private PostsListAdapter _adpater= null;
    private View _currentView;
    private int _page;
    private String _title;
    public static MobileServiceClient mClient;
    MobileServiceTable<User> mUserTable;
    MobileServiceTable<Post> mPostTable;
    MobileServiceTable<Comment> mCommentTable;

    private OnFragmentInteractionListener mListener;
    private ProgressBar mLoadinggProgressBar;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _currentView  = inflater.inflate(R.layout.fragment_posts, container, false);
        mLoadinggProgressBar = (ProgressBar) _currentView.findViewById(R.id.postFrag_progressBar);
        try {
            mClient = new MobileServiceClient(
                    "https://sallem.azurewebsites.net",
                    getActivity()).withFilter(new ProgressFilter());


            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient okHttpClient =new OkHttpClient();
                    okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
                    okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
                    return okHttpClient;
                }
            });


        }
        catch (MalformedURLException e){
            Log.d("SALLEMAPP", e.getCause().getMessage());

        }

        attachList();

        return _currentView;
    }



    private void attachList() {
        //_friendsList = loadPosts();
       // _adpater = new PostsListAdapter(this.getContext(), _friendsList);
        //setListAdapter(_adpater);
        loadPosts();

    }

    private void loadPosts() {

       LoadPosts p = new LoadPosts();
        p.delegate = this;
        p.execute();



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

        _adpater = new PostsListAdapter(this.getContext(), (ArrayList<DomainPost>) result);
        setListAdapter(_adpater);


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
    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mLoadinggProgressBar != null) mLoadinggProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {

                    resultFuture.setException(e);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mLoadinggProgressBar != null) mLoadinggProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mLoadinggProgressBar != null) mLoadinggProgressBar.setVisibility(ProgressBar.GONE);

                        }


                    });


                    resultFuture.set(response);

                }
            });

            return resultFuture;
        }
    }
    private class LoadPosts extends AsyncTask<Void, Void, List<DomainPost>> {
        public ListAsyncResult<DomainPost> delegate;
        @Override
        protected List<DomainPost> doInBackground(Void... params) {
            ArrayList<DomainPost> domainPosts = new ArrayList<>();
            mPostTable = mClient.getTable(Post.class);
            //MobileServiceTable<PostImage> imageTable = mClient.getTable(PostImage.class);
            MobileServiceTable<User> userTable = mClient.getTable(User.class);

            try {
                List<Post> posts = mPostTable.orderBy("postedAt", QueryOrder.Descending).execute().get();
                for (Post post : posts) {
//                    List<PostImage> images = imageTable.where()
//                            .field("postId").eq(post.getId()).execute().get();
                    DomainPost p = new DomainPost();
                    p.set_id(post.getId());
                    p.set_subject(post.getSubject());
                    p.set_postedAt(post.getPostedAt());

                    User user = userTable.where().field("id").eq(post.getUserId()).execute().get().get(0);
                    if (user != null) {
                        String imageTitle = user.getImageTitle() + ".jpg";
                        Bitmap avatar = DownloadImage.getImage(PostsFragment.this.getContext(), imageTitle);
                        DomainUser domainUser = new DomainUser(user);
                        domainUser.setAvatar(avatar);
                        p.set_user(domainUser);
                    }
                    String imagePath = post.get_imagePath();
                    if (imagePath != null ) {
                        p.set_image(getImage(imagePath));
                    }

                    domainPosts.add(p);
                }
            }
             catch (ExecutionException e) {
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());
                e.printStackTrace();

            } catch (InterruptedException e) {
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());


            } catch (URISyntaxException e) {
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());


            } catch (StorageException e) {
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());


            } catch (InvalidKeyException e) {
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());


            }
            catch (IOException e){
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());

            }

            return domainPosts;
        }


        private Bitmap getImage(String id) throws InvalidKeyException, URISyntaxException, StorageException, IOException {
            CloudStorageAccount account = CloudStorageAccount.parse(CommonMethods.storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference("sallemphotos");
            //container.createIfNotExists();

            // Upload an image file.
            //String imageName = UUID.randomUUID().toString();
            CloudBlockBlob blob = container.getBlockBlobReference(id);

            File outputDir = getActivity().getBaseContext().getCacheDir();
            File sourceFile = File.createTempFile("101", "jpg", outputDir);
            OutputStream outputStream = new FileOutputStream(sourceFile);
            //bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //outputStream.close();
            //blob.upload(new FileInputStream(sourceFile), sourceFile.length());

            // Download the image file.
            File destinationFile = new File(sourceFile.getParentFile(), "image1Download.tmp");
            blob.downloadToFile(destinationFile.getAbsolutePath());
            Bitmap image = BitmapFactory.decodeStream(new FileInputStream(destinationFile));
            return image;



        }


        @Override
        protected void onPostExecute(List<DomainPost> domainPosts) {
            delegate.processFinish(domainPosts);
        }
    }


}
