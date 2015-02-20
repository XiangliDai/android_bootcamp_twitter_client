package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {
    EditText editText;
    ImageView ivProfile;
    TextView tvUserName;
    TextView tvScreeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.etBody);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvScreeName = (TextView) findViewById(R.id.tvScreeName);
        User currentUser = TwitterApplication.getCurrentUser().getUser();
        Picasso.with(this).load(currentUser.getProfileImageUrl()).into(ivProfile);

        tvScreeName.setText(currentUser.getScreenName());
        tvUserName.setText(currentUser.getName());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tweet) {
            postTweet();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void postTweet() {
        TwitterApplication.getRestClient().postStatus(editText.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null && response.length() > 0) {
                    Tweet tweet = Tweet.fromJson(response);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("tweet", tweet);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                Log.e(ComposeActivity.class.getSimpleName(), "failed " + statusCode);
                //Toast.makeText(TAG, errorResponse.)
                try {
                    JSONArray errors = errorResponse.getJSONArray("errors");

                    JSONObject error = errors.getJSONObject(0);
                    String errorMessage = error.getString("message");
                    Toast.makeText(ComposeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
