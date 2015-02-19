package com.codepath.apps.mysimpletweets.models;

import com.codepath.apps.mysimpletweets.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tweet {

    private String body;
    private long uid;

    private String createdAt;

    public User getUser() {
        return user;
    }

    private User user;
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }


    public String getCreatedAt() {
        return createdAt;
    }


    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.has("text") ? jsonObject.getString("text") : "";
            tweet.uid = jsonObject.has("id") ? jsonObject.getLong("id") : 0;
            tweet.createdAt = jsonObject.has("created_at") ? Utils.getRelativeTimeAgo(jsonObject.getString("created_at")) : "";
            tweet.user = jsonObject.has("user")? User.fromJson(jsonObject.getJSONObject("user")): null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        if(jsonArray == null || jsonArray.length() < 1) return null;
        ArrayList<Tweet> tweets = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject resultJson = null;
            try {
                resultJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Tweet result = Tweet.fromJson(resultJson);
            if (result != null) {
                tweets.add(result);
            }
        }
        return tweets;
    }
}
