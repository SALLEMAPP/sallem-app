package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdul on 14-Apr-2017.
 */

public class CommentsView {
    @SerializedName("commentId")
    private String commentId ;

    @SerializedName("commentDate")
    private String commentDate ;

    @SerializedName("comment")
    private String comment ;

    @SerializedName("postId")
    private String postId ;

    @SerializedName("commenterId")
    private String commenterId ;

    @SerializedName("commenterFirstName")
    private String commenterFirstName ;

    @SerializedName("commenterLastName")
    private String commenterLastName ;

    @SerializedName("commenterImage")
    private String commenterImage ;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommenterFirstName() {
        return commenterFirstName;
    }

    public void setCommenterFirstName(String commenterFirstName) {
        this.commenterFirstName = commenterFirstName;
    }

    public String getCommenterLastName() {
        return commenterLastName;
    }

    public void setCommenterLastName(String commenterLastName) {
        this.commenterLastName = commenterLastName;
    }

    public String getCommenterImage() {
        return commenterImage;
    }

    public void setCommenterImage(String commenterImage) {
        this.commenterImage = commenterImage;
    }
}
