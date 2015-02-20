package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {
    private String name;
    private Long uid;
    private String screenName;

    private String profileImageUrl;

    public User(){}
    public User(Parcel in) {
        name = in.readString();
        screenName = in.readString();
        profileImageUrl = in.readString();
        uid = in.readLong();
    }

    public String getName() {
        return name;
    }

    public Long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(name);
        dest.writeString(screenName);
        dest.writeString(profileImageUrl);
        dest.writeLong(uid);
    }
    
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
