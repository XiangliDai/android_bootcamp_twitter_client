package com.codepath.apps.mysimpletweets;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.mysimpletweets.net.TwitterClient;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     RestClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TwitterApplication extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		TwitterApplication.context = this;
        ActiveAndroid.initialize(this);
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
	}


    public static CurrentUser getCurrentUser() {
        return (CurrentUser) CurrentUser.getInstance();
    }

}