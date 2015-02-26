package com.codepath.apps.mysimpletweets;

import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

public class CurrentUser {
    private static CurrentUser instance = null;


    private  User user;
    protected CurrentUser() {
        // Exists only to defeat instantiation.
    }
    public static CurrentUser getInstance() {
        if(instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public  void setUser(User user) {
        this.user = user;
    }
    public  User getUser(){
        return this.user;
        
    }

    public void requestCurrentUser() {
        TwitterApplication.getRestClient().getCurrentUserInformation(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (response != null && response.length() > 0) {
                    try {
                        User user = User.fromJson(response.getJSONObject(0));
                        TwitterApplication.getCurrentUser().setUser(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}