package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.mysimpletweets.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
@Table(name = "tweet")
public class Tweet extends Model implements Parcelable{
    @Column(name = "body")
    private String body;
    @Column(name = "uid")
    private long uid;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "favourites_count")
    private int favouritesCount;
    @Column(name = "retweet_count")
    private int retweetCount;
    @Column(name = "retween_name")
    private String retweenName;
    
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

    public String getRetweenName() {
        return retweenName;
    }

    public Tweet (){
        super();
    }
    
    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.has("text") ? jsonObject.getString("text") : "";
            tweet.uid = jsonObject.has("id") ? jsonObject.getLong("id") : 0;
           
            tweet.retweetCount = jsonObject.has("retweet_count") ? jsonObject.getInt("retweet_count") : 0;
            tweet.favouritesCount = jsonObject.has("favourites_count") ? jsonObject.getInt("favourites_count") : 0;
            User tUser = jsonObject.has("user") ? User.fromJson(jsonObject.getJSONObject("user")) : null;
            if(jsonObject.has("retweeted_status")) {
                JSONObject retweetJson = jsonObject.getJSONObject("retweeted_status");
                tweet.createdAt = retweetJson.has("created_at") ? Utils.getRelativeTimeString(retweetJson.getString("created_at")) : "";
                tweet.user = retweetJson.has("user") ? User.fromJson(retweetJson.getJSONObject("user")) : null;              
                tweet.retweenName =  tUser == null ? "" : tUser.getName();
                String regex = "RT @" + tweet.user.getScreenName() + ": " ;
                tweet.body = Pattern.compile(regex).matcher(tweet.body).replaceFirst("");
            }
            else{
                tweet.createdAt = jsonObject.has("created_at") ? Utils.getRelativeTimeString(jsonObject.getString("created_at")) : "";
                tweet.user =  tUser ;
            }

              tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.length() < 1) return null;
        ArrayList<Tweet> tweets = new ArrayList<>(jsonArray.length());
        ActiveAndroid.beginTransaction();
        try {
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
            ActiveAndroid.setTransactionSuccessful();
            return tweets;
        } finally {
            ActiveAndroid.endTransaction();
        }
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
        dest.writeString(retweenName);
        dest.writeParcelable(user, i);
    }

    public Tweet(Parcel in) {
        uid = in.readLong();
        createdAt = in.readString();
        body = in.readString();
        retweetCount = in.readInt();
        favouritesCount = in.readInt();
        retweenName = in.readString();
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

    public static List<Tweet> getAll() {
        return new Select()
                .from(Tweet.class)
                .orderBy("uid desc")
                .execute();
    }
}
