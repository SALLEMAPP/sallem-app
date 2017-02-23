package com.seniorproject.sallemapp.entities;

import org.joda.time.DateTime;

/**
 * Created by Centeral on 2/21/2017.
 */

public class Comment {
    private int _id;
    private DateTime _commentedAt;
    private int _userId;
    private String _content;
    private int _postId;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public DateTime getCommentedAt() {
        return _commentedAt;
    }

    public void setCommentedAt(DateTime commentedAt) {
        _commentedAt = commentedAt;
    }

    public int getUserId() {
        return _userId;
    }

    public void setUserId(int userId) {
        _userId = userId;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        _content = content;
    }

    public int getPostId() {
        return _postId;
    }

    public void setPostId(int postId) {
        _postId = postId;
    }
}
