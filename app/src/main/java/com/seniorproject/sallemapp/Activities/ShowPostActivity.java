package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Predicate;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.Activities.listsadpaters.CommentsListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.Comment;
import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.CommonMethods;
import com.seniorproject.sallemapp.helpers.AzureBlob;
import com.seniorproject.sallemapp.helpers.EntityAsyncResult;
import com.seniorproject.sallemapp.helpers.EntityAsyncResultTwo;
import com.seniorproject.sallemapp.helpers.MyApplication;
import com.seniorproject.sallemapp.helpers.MyHelper;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.LocalDateTime;

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

public class ShowPostActivity extends AppCompatActivity implements EntityAsyncResult<DomainPost>, EntityAsyncResultTwo<DomainComment> {

    ImageView mUserAvatart;
    TextView mPosDate;
    TextView mPoster;
    TextView mPostSubject;
    EditText mPostComment;
    ImageView mPostImage;
    ListView mCommentsList;
    ImageButton mSendCommentButton;
    CommentsListAdapter mCommentsAdapter;
    DomainPost mCurrentPost;
    MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        mUserAvatart = (ImageView) findViewById(R.id.showPost_imgUserAvatar);
        mPosDate = (TextView)
                findViewById(R.id.showPost_lblPostDate);
        mPoster = (TextView)
                findViewById(R.id.showPost_lblUserName);
        mPostSubject = (TextView)
                findViewById(R.id.showPost_txtPosSubject);
        mPostComment = (EditText)
                findViewById(R.id.showPost_txtComment);
        mPostImage = (ImageView)
                findViewById(R.id.showPost_imgPostImage);
        mSendCommentButton = (ImageButton)
                findViewById(R.id.showPost_btnSendComment);
        mCommentsList = (ListView)
                findViewById(R.id.showPost_listComments);
        attachSendCommentButton();
        ArrayList<DomainComment> comments =new ArrayList();
        mCommentsAdapter = new CommentsListAdapter(this , comments);
        mCommentsList.setAdapter(mCommentsAdapter);
        myApp = (MyApplication) getApplication();
        Bundle b = getIntent().getExtras();
        String postId = b.getString("postId");
        loadPost(postId);
    }

    private void attachSendCommentButton() {
        mSendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DomainComment domainComment = new DomainComment();
                domainComment.set_id(UUID.randomUUID().toString());
                String commentedAt = new LocalDateTime().toString();
                domainComment.set_commentedAt(commentedAt);
                domainComment.set_subject(mPostComment.getText().toString());
                domainComment.set_user(DomainUser.CURRENT_USER);
                domainComment.set_userId(DomainUser.CURRENT_USER.getId());
                domainComment.set_post(mCurrentPost);
                domainComment.set_posId(mCurrentPost.get_id());
                SaveCommentAsync savePostAsync = new
                        SaveCommentAsync(ShowPostActivity.this, domainComment);
                savePostAsync.Delegate = ShowPostActivity.this  ;
                savePostAsync.execute();


            }
        });

    }

    private void loadPost(final String postId) {
//        LoadPost asyncLoading = new LoadPost(this, postId);
//        asyncLoading.Delegate = this;
//        asyncLoading.execute();
        DomainPost showedPost = null;
         for(DomainPost p: myApp.Posts_Cach){
             if(p.get_id().equals(postId)){
                 showedPost = p;
                 break;
             }
         }
        processFinish(showedPost);

    }

    @Override
    public void processFinish(DomainPost result) {
        if(result != null){
         mPostImage.setImageBitmap(result.get_image());
            mPoster.setText(result.get_user().getFirstName() + " " + result.get_user().getLasttName());;
            mUserAvatart.setImageBitmap(result.get_user().getAvatar());;
            mPosDate.setText(result.get_postedAt());
            mPostSubject.setText(result.get_subject());
            mCommentsAdapter.addAll(result.get_comments());
            mCurrentPost = result;
        }
    }


    @Override
    public void processFinishTwo(DomainComment result) {
        mCommentsAdapter.add(result);
    }


    private class LoadPost extends AsyncTask<Void, Void, DomainPost>{

        public EntityAsyncResult<DomainPost> Delegate;
        private MobileServiceClient mClient;
        private MobileServiceTable<User> mUserTable;
        private MobileServiceTable<Post> mPostTable;
        private MobileServiceTable<Comment> mCommentTable;
        private String mId;
        private Context mContext;

        public LoadPost( Context context, String id){
            mContext = context;
            mId = id;
            try {
                mClient = MyHelper.getAzureClient(mContext);
            }
            catch (MalformedURLException e){
                Log.d("SALLEMAPP", e.getCause().getMessage());

            }


        }
        @Override
        protected DomainPost doInBackground(Void... params){
            mPostTable = mClient.getTable(Post.class);
            //MobileServiceTable<PostImage> imageTable = mClient.getTable(PostImage.class);
            mUserTable = mClient.getTable(User.class);
            mCommentTable = mClient.getTable(Comment.class);
            DomainPost p = null;
            try {
                Post post = mPostTable.where().field("id").eq(mId).execute().get().get(0);
                User user = mUserTable.where().field("id").eq(post.getUserId()).execute().get().get(0);
                List<Comment> comments = mCommentTable.where().field("postId").eq(post.getId()).execute().get();
                p = new DomainPost();
                p.set_id(post.getId());
                p.set_subject(post.getSubject());
                p.set_postedAt(post.getPostedAt());
                if (user != null) {
                    String imageTitle = user.getImageTitle() + ".jpg";
                    Bitmap avatar = AzureBlob.getImage(mContext, imageTitle );
                    DomainUser domainUser = new DomainUser(
                            user.getId(), user.getFirstName(), user.getLastName(),
                            user.getPassword(), user.getEmail(), user.getJoinedAt(),
                            user.getImageTitle(), user.getStatus(),
                            avatar, 0, 0, false
                    );
                        p.set_user(domainUser);
                    }
                    String imagePath = post.get_imagePath();
                    if (imagePath != null ) {
                        p.set_image(AzureBlob.getImage(mContext, imagePath));
                    }
                    if(comments != null){
                        ArrayList<DomainComment> doaminComments = new ArrayList<>();

                        for (Comment comment : comments){
                            DomainComment domainComment = new DomainComment();
                            domainComment.set_id(comment.get_id());
                            domainComment.set_commentedAt(comment.get_commentedAt());
                            domainComment.set_subject(comment.get_subject());
                            User userCommented = mUserTable.where().field("id").eq(comment.get_userId()).execute().get().get(0);
                            if (userCommented != null) {
                                String imageTitle = userCommented.getImageTitle() + ".jpg";
                                Bitmap avatar = AzureBlob.getImage(mContext, imageTitle);

                                DomainUser commentedDomainUser = new DomainUser(
                                        user.getId(), user.getFirstName(), user.getLastName(),
                                        user.getPassword(), user.getEmail(), user.getJoinedAt(),
                                        user.getImageTitle(), user.getStatus(),
                                        avatar, 0, 0, false
                                );
                                domainComment.set_user(commentedDomainUser);
                            }
                            doaminComments.add(domainComment);

                        }
                        p.set_comments( doaminComments);
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

            return p;
        }





        @Override
        protected void onPostExecute(DomainPost domainPost) {
            Delegate.processFinish(domainPost);
        }
    }

    private class SaveCommentAsync extends AsyncTask<Void, Void, DomainComment>{

        public EntityAsyncResultTwo<DomainComment> Delegate;
        private MobileServiceClient mClient;
        private MobileServiceTable<Comment> mCommentTable;
        private DomainComment mComment;
        private Context mContext;

        public SaveCommentAsync( Context context, DomainComment comment){
            mContext = context;
            mComment = comment;
            try {
                mClient = new MobileServiceClient(
                        "https://sallem.azurewebsites.net",
                        context);
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


        }
        @Override
        protected DomainComment doInBackground(Void... params){
            mCommentTable = mClient.getTable(Comment.class);

            try {
                Comment dbComment = new Comment();
                dbComment.set_id(mComment.get_id());
                dbComment.set_commentedAt(mComment.get_commentedAt());
                dbComment.set_userId(mComment.get_userId());
                dbComment.set_postId(mComment.get_posId());
                dbComment.set_subject(mComment.get_subject());

                mCommentTable.insert(dbComment).get();

            }
            catch (ExecutionException e) {
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());
                e.printStackTrace();

            } catch (InterruptedException e) {
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());
            }


            return mComment;
        }
        @Override
        protected void onPostExecute(DomainComment domainComment) {
            Delegate.processFinishTwo(domainComment);
        }
    }




}
