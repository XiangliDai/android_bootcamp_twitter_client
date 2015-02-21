package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.codepath.apps.mysimpletweets.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tweet implements Parcelable{

    private String body;
    private long uid;
    private String createdAt;
    private User user;

    private int favouritesCount;
   private int retweetCount;
    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }


    public int getFavouritesCount() {
        return favouritesCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Tweet (){}
    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.has("text") ? jsonObject.getString("text") : "";
            tweet.uid = jsonObject.has("id") ? jsonObject.getLong("id") : 0;
            tweet.createdAt = jsonObject.has("created_at") ? Utils.getRelativeTimeString(jsonObject.getString("created_at")) : "";
            tweet.retweetCount = jsonObject.has("retweet_count") ? jsonObject.getInt("retweet_count") : 0;
            tweet.favouritesCount = jsonObject.has("favourites_count") ? jsonObject.getInt("favourites_count") : 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(uid);
        dest.writeString(createdAt);
        dest.writeString(body);
        dest.writeInt(retweetCount);
        dest.writeInt(favouritesCount);
        dest.writeParcelable(user, i);
    }

    public Tweet(Parcel in) {
        uid = in.readLong();
        createdAt = in.readString();
        body = in.readString();
        retweetCount = in.readInt();
        favouritesCount = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
