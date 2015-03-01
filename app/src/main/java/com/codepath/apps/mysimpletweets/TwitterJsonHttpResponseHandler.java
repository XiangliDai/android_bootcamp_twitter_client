package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterJsonHttpResponseHandler extends JsonHttpResponseHandler {
    private Context context;
    public TwitterJsonHttpResponseHandler(Context context){
        this.context = context;
    }
    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
            errorResponse) {
        Log.e(TwitterJsonHttpResponseHandler.class.getSimpleName(), "failed " + statusCode);
        try {
            if(errorResponse != null && errorResponse.has("errors")) {
                JSONArray errors = errorResponse.getJSONArray("errors");

                JSONObject error = errors.getJSONObject(0);
                String errorMessage = error.getString("message");
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onFailure(int statusCode, Header[] headers, String message, Throwable throwable) {
        Log.e(TwitterJsonHttpResponseHandler.class.getSimpleName(), "failed " + statusCode);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
       
    }
}
