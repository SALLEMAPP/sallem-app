package com.seniorproject.sallemapp.helpers;

import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.Post;

import java.util.List;

/**
 * Created by abdul on 04-Apr-2017.
 */

public interface RefreshedPostsResult {
    void onGotResult(List<DomainPost> result);
}
