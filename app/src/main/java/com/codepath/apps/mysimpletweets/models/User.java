package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private Long uid;
    private String screenName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    private String profileImageUrl;


    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.has("name") ? jsonObject.getString("name") : "";
            user.uid = jsonObject.has("id") ? jsonObject.getLong("id") : 0;
            user.screenName = jsonObject.has("screen_name") ? jsonObject.getString("screen_name") : "";
            user.profileImageUrl = jsonObject.has("profile_image_url") ? jsonObject.getString("profile_image_url") : "";
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }
}
