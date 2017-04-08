package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.Comment;
import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.FriendPost;
import com.seniorproject.sallemapp.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by abdul on 05-Apr-2017.
 */

public class LoadFriendsPostsAsync {
    private Context mContext;
    public boolean finished;
    private RefreshedPostsResult mCallback;
    private MobileServiceClient mClient;

    public LoadFriendsPostsAsync(Context context, RefreshedPostsResult callback) {
        mContext = context;
        mCallback = callback;
    }

    public void LoadAsync(String id) {
        try {
            mClient =  MyHelper.getAzureClient(mContext);
            List<Pair<String, String>> p = new ArrayList<>();
            Pair<String, String> pair = new Pair<>("id", id);
            p.add(pair);
            ListenableFuture<FriendPost[]> posts =
                    mClient.invokeApi("friendsposts", "GET", p, FriendPost[].class);
            Futures.addCallback(posts, new FutureCallback<FriendPost[]>() {
                @Override
                public void onSuccess(FriendPost[] apiResult) {
                    if (apiResult != null) {
                        final FriendPost[] myResult = apiResult;
                        AsyncTask task = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] params) {
                                try {
                                    List<DomainPost> domainPosts = transformPosts(myResult);
                                    for (DomainPost p : domainPosts) {
                                        DomainUser user = getDomainUser(p.get_userId());
                                        p.set_user(user);
                                        List<DomainComment> comments = getPostComments(p.get_id(), p);
                                       p.set_comments(comments);

                                        //String imagePath = p.getImagePath();
//                                        if (imagePath != null) {
//                                            Bitmap bm = AzureBlob.getImage(mContext, imagePath);
//                                            p.set_image(bm);
//                                        }
                                        if(p.getImagePath() != null){
                                            Bitmap image = MyHelper.decodeImage(p.getImagePath());
                                            p.set_image(image);
                                        }
                                    }
                                    onFinish(domainPosts);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("SALLEMAPP", "doInBackground: "+e.getMessage());
                                    onFinish(null);
                                }
                                return null;
                            }
                        };
                        task.execute();
                    }

                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.e("SALLEMAPP", "onFailure: " + throwable.getMessage());
                    onFinish(null);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SALLAPP", "doInBackground:" + e.getCause().getMessage());
        }
    }

    private DomainUser getDomainUser(String userId) throws InterruptedException, ExecutionException {
        MobileServiceTable<User> userTable = mClient.getTable(User.class);
        DomainUser domainUser = null;
        User user = userTable.where().field("id").eq(userId).execute().get().get(0);
        if (user != null) {
            Bitmap avatar = null;
            String imageTitle = user.getImageTitle();
//            if (user.getImageTitle().equals(MyHelper.DEFAULT_AVATAR_TITLE)) {
//                avatar = MyHelper.getDefaultAvatar(mContext);
//            } else {
//                String imageTitle = user.getImageTitle() + ".jpg";
//                try {
//                    avatar = AzureBlob.getImage(mContext, imageTitle);
//                } catch (Exception e) {
//                }
//            }
            if(!imageTitle.equals(MyHelper.DEFAULT_AVATAR_TITLE)) {
                // try {
                //In case no avatar, just fail gracefully.
                // imageTitle = user.getImageTitle() + ".jpg";
                //avatar = AzureBlob.getImage(mContext, imageTitle);
                // } catch (StorageException e) {
                //e.printStackTrace();
                //Log.e("SALLEM APP", "doInBackground: " + e.getCause().getMessage());

                //}
                avatar = MyHelper.decodeImage(imageTitle);
            }
            else{
                avatar =  MyHelper.getDefaultAvatar(mContext);
            }
            domainUser = new DomainUser(
                    user.getId(), user.getFirstName(), user.getLastName(),
                    user.getPassword(), user.getEmail(), user.getJoinedAt(),
                    user.getImageTitle(), user.getStatus(),
                    avatar, 0, 0, false);
        }
        return domainUser;
    }

    private List<DomainComment> getPostComments(String postId, DomainPost post) throws InterruptedException, ExecutionException {
        List<DomainComment> domainComments = new ArrayList<>();
        MobileServiceTable<Comment> commentTable = mClient.getTable(Comment.class);
        List<Comment> comments = commentTable.where().field("postId").eq(postId).execute().get();
        if (comments != null && comments.size() > 0) {
            for (Comment c : comments) {
                DomainComment domainComment = new DomainComment();
                domainComment.set_id(c.get_id());
                domainComment.set_commentedAt(c.get_commentedAt());
                domainComment.set_userId(c.get_userId());
                domainComment.set_subject(c.get_subject());
                domainComment.set_posId(c.get_postId());
                domainComment.set_post(post);
                DomainUser user = getDomainUser(c.get_userId());
                domainComment.set_user(user);
                domainComments.add(domainComment);
            }
        }
        return domainComments;
    }

    private List<DomainPost> transformPosts(FriendPost[] result) {
        List<DomainPost> posts = new ArrayList<>();
        for (int i = 0; i < result.length; i++) {
            DomainPost post = new DomainPost();
            FriendPost friendPost = result[i];
            post.set_id(friendPost.getId());
            post.set_postedAt(friendPost.getPostedAt());
            post.set_subject(friendPost.getSubject());
            post.set_userId(friendPost.getUserId());
            post.set_activityId(friendPost.getActivityId());
            //post.setImagePath(friendPost.get_imagePath());
            post.setImagePath(friendPost.getPostImage());
            posts.add(post);
        }
        return posts;
    }

    private void onFinish(List<DomainPost> result) {
        if (mCallback != null) {
            mCallback.onGotResult(result);
        }
    }


}
