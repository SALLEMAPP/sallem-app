package com.seniorproject.sallemapp.Activities.dbhelpers;

import com.seniorproject.sallemapp.entities.User;

/**
 * Created by Centeral on 2/21/2017.
 */

public class DbContext {
    public User getUser(int id){
        return User.CURRENT_USER;

    }
}
