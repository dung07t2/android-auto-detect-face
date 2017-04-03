package com.termskip;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by DzEagle on 11/5/16.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String uid;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

}
