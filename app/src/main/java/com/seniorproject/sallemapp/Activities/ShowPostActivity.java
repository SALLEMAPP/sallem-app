package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.seniorproject.sallemapp.helpers.DownloadImage;
import com.seniorproject.sallemapp.helpers.EntityAsyncResult;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ShowPostActivity extends AppCompatActivity implements EntityAsyncResult<DomainPost> {

    ImageView mUserAvatart;
    TextView mPosDate;
    TextView mPoster;
    TextView mPostSubject;
    ImageView mPostImage;
    ImageButton mSendCommentButton;
    CommentsListAdapter mCommentsAdapter;

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
        mPostImage = (ImageView)
                findViewById(R.id.showPost_imgPostImage);
        mSendCommentButton = (ImageButton)
                findViewById(R.id.showPost_btnSendComment);
        attachSendCommentButton();
        ArrayList<DomainComment> comments =new ArrayList();
        mCommentsAdapter = new CommentsListAdapter(this , comments);

        Bundle b = getIntent().getExtras();
        String postId = b.getString("postId");
        loadPost(postId);
    }

    private void attachSendCommentButton() {
        mSendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void loadPost(String postId) {
        LoadPost asyncLoading = new LoadPost(this, postId);
        asyncLoading.Delegate = this;
        asyncLoading.execute();
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


        }
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
                    Bitmap avatar = DownloadImage.getImage(mContext, imageTitle);
                    DomainUser domainUser = new DomainUser(user);
                        domainUser.setAvatar(avatar);
                        p.set_user(domainUser);
                    }
                    String imagePath = post.get_imagePath();
                    if (imagePath != null ) {
                        p.set_image(getImage(imagePath));
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
                                Bitmap avatar = DownloadImage.getImage(mContext, imageTitle);
                                DomainUser commentedDomainUser = new DomainUser(user);
                                commentedDomainUser.setAvatar(avatar);
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


        private Bitmap getImage(String id) throws InvalidKeyException, URISyntaxException, StorageException, IOException {
            CloudStorageAccount account = CloudStorageAccount.parse(CommonMethods.storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference("sallemphotos");
            //container.createIfNotExists();

            // Upload an image file.
            //String imageName = UUID.randomUUID().toString();
            CloudBlockBlob blob = container.getBlockBlobReference(id);

            File outputDir = mContext.getCacheDir();
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
        protected void onPostExecute(DomainPost domainPost) {
            Delegate.processFinish(domainPost);
        }
    }



}
