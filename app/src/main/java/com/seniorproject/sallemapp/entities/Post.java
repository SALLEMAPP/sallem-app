package com.seniorproject.sallemapp.entities;

import org.joda.time.DateTime;

/**
 * Created by Centeral on 2/21/2017.
 */

public class Post {
    private String _postId;
    private String _postedAt;
    private String _content;
    private int _userId;
    private int _activityId;
    private User _user;
    private Activity _activity;

    public String getPostId() {
        return _postId;
    }

    public void setPostId(String postId) {
        _postId = postId;
    }

    public String getPostedAt() {
        return _postedAt;
    }

    public void setPostedAt(String postedAt) {
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


    public User getUser() {
        return _user;
    }

    public void setUser(User _user) {
        this._user = _user;
    }

    public Activity get_activity() {
        return _activity;
    }

    public void set_activity(Activity _activity) {
        this._activity = _activity;
    }
}
