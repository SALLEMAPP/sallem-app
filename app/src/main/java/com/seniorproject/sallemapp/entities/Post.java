package com.seniorproject.sallemapp.entities;

import org.joda.time.DateTime;

/**
 * Created by Centeral on 2/21/2017.
 */

public class Post {
    private int _postId;
    private DateTime _postedAt;
    private String _content;
    private int _userId;
    private int _activityId;

    public int getPostId() {
        return _postId;
    }

    public void setPostId(int postId) {
        _postId = postId;
    }

    public DateTime getPostedAt() {
        return _postedAt;
    }

    public void setPostedAt(DateTime postedAt) {
        _postedAt = postedAt;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        _content = content;
    }

    public int getUserId() {
        return _userId;
    }

    public void setUserId(int userId) {
        _userId = userId;
    }

    public int getActivityId() {
        return _activityId;
    }

    public void setActivityId(int activityId) {
        _activityId = activityId;
    }
}
