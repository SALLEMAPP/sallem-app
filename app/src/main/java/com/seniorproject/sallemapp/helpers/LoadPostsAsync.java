package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.seniorproject.sallemapp.entities.Comment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.User;
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

/**
 * Created by abdul on 11-Mar-2017.
 */

public class LoadPostsAsync extends AsyncTask<Void, Void, List<DomainPost>> {
    public ListAsyncResult<DomainPost> delegate;
    private RefreshedPostsResult mCallback;
    public static MobileServiceClient mClient;
    MobileServiceTable<User> mUserTable;
    MobileServiceTable<Post> mPostTable;
    MobileServiceTable<Comment> mCommentTable;
    Context mContext;
    String mLastRefresh;
    MyApplication myApp;
    public LoadPostsAsync(Context context, String lastRefresh, RefreshedPostsResult callback, MyApplication app){
        mContext = context;
        mLastRefresh = lastRefresh;
        mCallback =callback;
        myApp =app;

    }
    public LoadPostsAsync( Context context, ServiceFilter progressFilter){
        mContext = context;
        try {
            mClient = MyHelper.getAzureClient(mContext);
        }
        catch (MalformedURLException e){
            Log.d("LoadPostsAsync", e.getCause().getMessage());

        }


    }
    @Override
    protected List<DomainPost> doInBackground(Void... params) {
        ArrayList<DomainPost> domainPosts = new ArrayList<>();

        try {
            MobileServiceClient client =  MyHelper.getAzureClient(mContext);
            mPostTable = client.getTable(Post.class);
            //MobileServiceTable<PostImage> imageTable = mClient.getTable(PostImage.class);
            MobileServiceTable<User> userTable = client.getTable(User.class);
            List<Post> posts;
            if(mLastRefresh == null) {
                posts = mPostTable.orderBy("postedAt", QueryOrder.Descending).execute().get();
            }
            else{
                posts = mPostTable.where().field("postedAt").gt(mLastRefresh)
                        .orderBy("postedAt", QueryOrder.Descending).execute().get();
            }
            Log.e("SALLEM-LOAD POSTS ASYNC", String.valueOf(posts.size()));
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
                    Bitmap avatar = AzureBlob.getImage(mContext, imageTitle);
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
    protected void onPostExecute(List<DomainPost> domainPosts) {
       if(delegate != null) {
           delegate.processFinish(domainPosts);
       }
       if(mCallback != null){
           mCallback.onGotResult(domainPosts);
       }
    }


}
