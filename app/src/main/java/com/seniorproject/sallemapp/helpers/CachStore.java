package com.seniorproject.sallemapp.helpers;

import com.seniorproject.sallemapp.entities.Comment;
import com.seniorproject.sallemapp.entities.DomainComment;
import com.seniorproject.sallemapp.entities.DomainFriendship;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.Friendship;

import java.util.List;

/**
 * Created by abdul on 04-Apr-2017.
 */

public class CachStore {
    public static List<DomainPost> POSTS_CACH;
    public static List<DomainComment> COMMENTS_CACH;
    public static List<DomainFriendship> FRIENDS_REQUESTS;
}
