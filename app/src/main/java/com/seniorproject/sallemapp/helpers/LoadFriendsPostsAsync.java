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
import com.seniorproject.sallemapp.entities.CommentsView;
import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.FriendPost;
import com.seniorproject.sallemapp.entities.PostsView;
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

    public void LoadAsync(String id, String lastUpdate) {
        try {
            mClient =  MyHelper.getAzureClient(mContext);
            List<Pair<String, String>> params = new ArrayList<>();
            Pair<String, String> parm1 = new Pair<>("id", id);
            Pair<String, String> parm2 = new Pair<>("update", lastUpdate);
            params.add(parm1);
            params.add(parm2);
            ListenableFuture<PostsView[]> posts =
                    mClient.invokeApi("postsview", "GET", params, PostsView[].class);
            Futures.addCallback(posts, new FutureCallback<PostsView[]>() {
                @Override
                public void onSuccess(PostsView[] apiResult) {
                    if (apiResult != null) {
                        final PostsView[] myResult = apiResult;
                        List<DomainPost> domainPosts = transformPosts(myResult);
                        onFinish(domainPosts);
                    }
                }
                @Override
                public void onFailure(Throwable throwable) {
                    Log.e("LoadFriendsPostsAsync", "onFailure: " + throwable.getMessage());
                    onFinish(null);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LoadFriendsPostsAsync", "doInBackground:" + e.getCause().getMessage());
        }
    }
    private void onFinish(List<DomainPost> result) {
        if (mCallback != null) {
            mCallback.onGotResult(result);
        }
    }
    private List<DomainPost> transformPosts(PostsView[] result) {
        List<DomainPost> posts = new ArrayList<>();
        for (int i = 0; i < result.length; i++) {
            DomainPost post = new DomainPost();
            PostsView friendPost = result[i];
            post.set_id(friendPost.getPostId());
            post.set_postedAt(friendPost.getPostedAt());
            post.set_subject(friendPost.getSubject());
            post.set_userId(friendPost.getPosterId());
            if(friendPost.getPostImage() != null){
                Bitmap image = MyHelper.decodeImage(friendPost.getPostImage());
                post.set_image(image);
            }
            Bitmap userAvatar = null;
            if(friendPost.getPosterImage() != null && !friendPost.getPosterImage().equals(MyHelper.DEFAULT_AVATAR_TITLE)){
                userAvatar = MyHelper.decodeImage(friendPost.getPosterImage());
            }
            else{
                userAvatar = MyHelper.getDefaultAvatar(mContext);
            }
            DomainUser poster = new DomainUser(friendPost.getPosterId(),
                            friendPost.getPosterFirstName(), friendPost.getPosterFirstName(),
                            null, null, null,null, -1, userAvatar,0,0,false);
            post.set_user(poster);
            List<DomainComment> postComments = new ArrayList<>();
            if(friendPost.getPostComments() != null){
                for(CommentsView commentView: friendPost.getPostComments()){
                    DomainComment comment = new DomainComment();
                    comment.set_id(commentView.getCommentId());
                    comment.set_commentedAt(commentView.getCommentDate());
                    comment.set_subject(commentView.getComment());
                    comment.set_posId(commentView.getPostId());
                    comment.set_userId(commentView.getCommenterId());
                    Bitmap commenterAvatar = null;
                    if(commentView.getCommenterImage() != null
                            && !commentView.getCommenterImage().equals(MyHelper.DEFAULT_AVATAR_TITLE)){
                        commenterAvatar = MyHelper.decodeImage(commentView.getCommenterImage());
                    }
                    else{
                        commenterAvatar = MyHelper.getDefaultAvatar(mContext);
                    }
                    DomainUser commenter = new DomainUser(commentView.getCommenterId(),
                            commentView.getCommenterFirstName(), commentView.getCommenterLastName(),
                            null, null, null,null, -1, commenterAvatar,0,0,false);
                    comment.set_user(commenter);
                    postComments.add(comment);
                }
            }
            post.set_comments(postComments);
            posts.add(post);
        }
        return posts;
    }

//    private DomainUser getDomainUser(String userId) throws InterruptedException, ExecutionException {
//        MobileServiceTable<User> userTable = mClient.getTable(User.class);
//        DomainUser domainUser = null;
//        User user = userTable.where().field("id").eq(userId).execute().get().get(0);
//        if (user != null) {
//            Bitmap avatar = null;
//            String imageTitle = user.getImageTitle();
////            if (user.getImageTitle().equals(MyHelper.DEFAULT_AVATAR_TITLE)) {
////                avatar = MyHelper.getDefaultAvatar(mContext);
////            } else {
////                String imageTitle = user.getImageTitle() + ".jpg";
////                try {
////                    avatar = AzureBlob.getImage(mContext, imageTitle);
////                } catch (Exception e) {
////                }
////            }
//            if(!imageTitle.equals(MyHelper.DEFAULT_AVATAR_TITLE)) {
//                // try {
//                //In case no avatar, just fail gracefully.
//                // imageTitle = user.getImageTitle() + ".jpg";
//                //avatar = AzureBlob.getImage(mContext, imageTitle);
//                // } catch (StorageException e) {
//                //e.printStackTrace();
//                //Log.e("SALLEM APP", "doInBackground: " + e.getCause().getMessage());
//
//                //}
//                avatar = MyHelper.decodeImage(imageTitle);
//            }
//            else{
//                avatar =  MyHelper.getDefaultAvatar(mContext);
//            }
//            domainUser = new DomainUser(
//                    user.getId(), user.getFirstName(), user.getLastName(),
//                    user.getPassword(), user.getEmail(), user.getJoinedAt(),
//                    user.getImageTitle(), user.getStatus(),
//                    avatar, 0, 0, false);
//        }
//        return domainUser;
//    }

//    private List<DomainComment> getPostComments(String postId, DomainPost post) throws InterruptedException, ExecutionException {
//        List<DomainComment> domainComments = new ArrayList<>();
//        MobileServiceTable<Comment> commentTable = mClient.getTable(Comment.class);
//        List<Comment> comments = commentTable.where().field("postId").eq(postId).execute().get();
//        if (comments != null && comments.size() > 0) {
//            for (Comment c : comments) {
//                DomainComment domainComment = new DomainComment();
//                domainComment.set_id(c.get_id());
//                domainComment.set_commentedAt(c.get_commentedAt());
//                domainComment.set_userId(c.get_userId());
//                domainComment.set_subject(c.get_subject());
//                domainComment.set_posId(c.get_postId());
//                domainComment.set_post(post);
//                DomainUser user = getDomainUser(c.get_userId());
//                domainComment.set_user(user);
//                domainComments.add(domainComment);
//            }
//        }
//        return domainComments;
//    }




//    public void LoadAsync(String id) {
//        try {
//            mClient =  MyHelper.getAzureClient(mContext);
//            List<Pair<String, String>> p = new ArrayList<>();
//            Pair<String, String> pair = new Pair<>("id", id);
//            p.add(pair);
//            ListenableFuture<FriendPost[]> posts =
//                    mClient.invokeApi("friendsposts", "GET", p, FriendPost[].class);
//            Futures.addCallback(posts, new FutureCallback<FriendPost[]>() {
//                @Override
//                public void onSuccess(FriendPost[] apiResult) {
//                    if (apiResult != null) {
//                        final FriendPost[] myResult = apiResult;
//                        AsyncTask task = new AsyncTask() {
//                            @Override
//                            protected Object doInBackground(Object[] params) {
//                                try {
//                                    List<DomainPost> domainPosts = transformPosts(myResult);
//                                    for (DomainPost p : domainPosts) {
//                                        DomainUser user = getDomainUser(p.get_userId());
//                                        p.set_user(user);
//                                        List<DomainComment> comments = getPostComments(p.get_id(), p);
//                                       p.set_comments(comments);
//
//                                        if(p.getImagePath() != null){
//                                            Bitmap image = MyHelper.decodeImage(p.getImagePath());
//                                            p.set_image(image);
//                                        }
//                                    }
//                                    onFinish(domainPosts);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    Log.e("SALLEMAPP", "doInBackground: "+e.getMessage());
//                                    onFinish(null);
//                                }
//                                return null;
//                            }
//                        };
//                        task.execute();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Throwable throwable) {
//                    Log.e("SALLEMAPP", "onFailure: " + throwable.getMessage());
//                    onFinish(null);
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("SALLAPP", "doInBackground:" + e.getCause().getMessage());
//        }
//    }


}
