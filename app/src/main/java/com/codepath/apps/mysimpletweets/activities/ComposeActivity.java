package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterJsonHttpResponseHandler;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {
    EditText editText;
    ImageView ivProfile;
    TextView tvUserName;
    TextView tvScreeName;

    TextView toolbar_text;
    Long tweetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_text = (TextView)toolbar.findViewById(R.id.toolbar_text);
        
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        toolbar.setLogo(R.mipmap.ic_launcher);
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        tweetId = getIntent().getLongExtra("tweetId", 0);
        String screenName = getIntent().getStringExtra("screenName");

        editText = (EditText) findViewById(R.id.etBody);
        if(screenName!=null && !screenName.isEmpty()){
            editText.setText(screenName + " ");
        }
        editText.setSelection(editText.getText().length());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                toolbar_text.setText(140 - s.length()+ "");
            }
        });
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvScreeName = (TextView) findViewById(R.id.tvScreeName);
        User currentUser = TwitterApplication.getCurrentUser().getUser();
        if(currentUser!= null) {
            Picasso.with(this).load(currentUser.getProfileImageUrl()).into(ivProfile);

            tvScreeName.setText(currentUser.getScreenName());
            tvUserName.setText(currentUser.getName());
        }
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
        TwitterApplication.getRestClient().postStatus(editText.getText().toString(), tweetId, new TwitterJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null && response.length() > 0) {
                    Tweet tweet = Tweet.fromJson(response);
                    Log.d(ComposeActivity.class.getSimpleName(), "tweet retweet count: " + tweet.getRetweetCount());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("tweet", tweet);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

    }
}
