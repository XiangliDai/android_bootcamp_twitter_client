package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterJsonHttpResponseHandler;
import com.codepath.apps.mysimpletweets.adapters.TweetAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity {
    private static final String TAG = TimelineActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 10;
    private ListView lvList;
    private List<Tweet> tweetList;
    private SwipeRefreshLayout swipeContainer;
    private TweetAdapter tweetAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.mipmap.ic_launcher);
        
        getSupportActionBar().setTitle(getString(R.string.title_activity_timeline));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        lvList = (ListView) findViewById(R.id.lvList);
        tweetList = new ArrayList<>();
        tweetAdapter = new TweetAdapter(this, tweetList);
        lvList.setAdapter(tweetAdapter);
        lvList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getOlderTimeline();

            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewerTimeline();
            }
        });
        
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getNewerTimeline();
        if(TwitterApplication.getCurrentUser().getUser()== null) {
            getCurrentUser();
        }
    }

    private void getCurrentUser() {
        TwitterApplication.getRestClient().getCurrentUserInformation(new TwitterJsonHttpResponseHandler(this) {
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

    private void getNewerTimeline() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            currentId = tweetList.get(0).getUid();
        }
        TwitterApplication.getRestClient().getNewerTimelineList(currentId, getJsonHttpResponseHandler());
    }

    private void getOlderTimeline() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            int index = tweetList.size() - 1;
            currentId = tweetList.get(index).getUid();
        }
        TwitterApplication.getRestClient().getOlderTimelineList(currentId, getJsonHttpResponseHandler());

    }

    private JsonHttpResponseHandler getJsonHttpResponseHandler() {
        return new TwitterJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                if (response != null && response.length() > 0) {
                    tweetList.addAll(Tweet.fromJsonArray(response));
                    tweetAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
            launchComposer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchComposer() {
        Intent intent = new Intent(this, ComposeActivity.class);
        
        startActivityForResult(intent, REQUEST_CODE);

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE  && resultCode == RESULT_OK) {
            Tweet tweet = data.getParcelableExtra("tweet");
            if(tweet != null ){
                if(tweetList == null) tweetList = new ArrayList<>();
                tweetList.add(0 ,tweet);
                tweetAdapter.notifyDataSetChanged();
            }
        }
    }
}
