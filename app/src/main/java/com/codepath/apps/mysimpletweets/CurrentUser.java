package com.codepath.apps.mysimpletweets;

import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

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
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    User user = User.fromJson(response);
                    TwitterApplication.getCurrentUser().setUser(user);

                }
            }
        });
    }
}