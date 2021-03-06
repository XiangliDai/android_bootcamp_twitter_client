package com.codepath.apps.mysimpletweets.net;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "VrKKVOeZiD1hxhnhVetZ7TiKD";       // Change this
	public static final String REST_CONSUMER_SECRET = "UzX5hhwvsHHku3gMebcjC4InG565h8Exg6lgsO0zkEpConWBEI"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)
    public static final Long CURRENT_USER_ID = 165651913L;//3051344827L; // Long.valueOf(165651913);
    public static int COUNT_PER_CALL = 25;
	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getUserInformation(Long userId, AsyncHttpResponseHandler handler){
        Log.d(TwitterClient.class.getSimpleName(), "getUserInformation userId: " + userId);
        String apiUrl = getApiUrl("users/show.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        client.get(apiUrl, params, handler);

    }


    public void getCurrentUserInformation(AsyncHttpResponseHandler handler){
        getUserInformation(CURRENT_USER_ID, handler);
    }
    
    public void getNewerTimelineList(Long sinceId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count", COUNT_PER_CALL);
        params.put("since_id", sinceId);
        client.get(apiUrl, params, handler);

    }
    
    public void getOlderTimelineList(Long maxId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count", COUNT_PER_CALL);
        params.put("max_id", maxId);
        client.get(apiUrl, params, handler);
        
    }
    
    public void postStatus(String status, Long tweetId, ResponseHandlerInterface handler){
        String apiUrl = getApiUrl("statuses/update.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("status", status);
        if(tweetId!=0) {
            params.put("in_reply_to_status_id", tweetId);
        }
        client.post(apiUrl, params, handler);
    }
    
    public void getNewerMentionsList(Long sinceId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count", COUNT_PER_CALL);
        params.put("since_id", sinceId);
        client.get(apiUrl, params, handler);
        
    }
    
    public void getOlderMentionsList(Long maxId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count", COUNT_PER_CALL);
        params.put("since_id", maxId);
        client.get(apiUrl, params, handler);
        
    }


    public void getNewerUserTimelineList(Long userId, Long sinceId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("count", COUNT_PER_CALL);
        params.put("since_id", sinceId);
        client.get(apiUrl, params, handler);

    }

    public void getOlderUserTimelineList(long userId, Long maxId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("count", COUNT_PER_CALL);
        params.put("max_id", maxId);
        client.get(apiUrl, params, handler);

    }

    public void getNewerSearchList(String query, Long sinceId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("search/tweets.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("q", query);
        params.put("count", COUNT_PER_CALL);
        params.put("since_id", sinceId);
        client.get(apiUrl, params, handler);

    }

    public void getOlderSearchList(String query, Long maxId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("search/tweets.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("q", query);
        params.put("count", COUNT_PER_CALL);
        params.put("max_id", maxId);
        client.get(apiUrl, params, handler);

    }
    
    public void retweet(Long id, AsyncHttpResponseHandler handler){
        String url = String.format("statuses/retweet/%d.json", id);
        String apiUrl = getApiUrl(url);
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }

    public void favorite(Long id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("favorites/create.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }

    public void unfavorite(Long id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("favorites/destroy.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);

    }
	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}