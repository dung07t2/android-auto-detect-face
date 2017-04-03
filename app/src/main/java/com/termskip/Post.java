package com.termskip;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DzEagle on 11/5/16.
 */

@IgnoreExtraProperties
public class Post {

    public String uid;
    public String deviceId;
    public String urlImage;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String deviceId, String urlImage) {
        this.uid = uid;
        this.deviceId = deviceId;
        this.urlImage = urlImage;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("deviceId", deviceId);
        result.put("urlImage", urlImage);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

}