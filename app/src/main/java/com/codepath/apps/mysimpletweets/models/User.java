package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;
@Table(name = "user")

public class User extends Model implements Parcelable {
    @Column(name = "name")
    private String name;
    @Column(name = "uid")
    private Long uid;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    public User(){
        super();
        
    }
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
            user.save();
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
