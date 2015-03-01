package com.codepath.apps.mysimpletweets.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweets.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterJsonHttpResponseHandler;
import com.codepath.apps.mysimpletweets.Utils;
import com.codepath.apps.mysimpletweets.activities.ComposeActivity;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.adapters.TweetAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.net.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public abstract class TwitterBaseFragment extends Fragment implements
        TweetAdapter.IActionClickListener {
    private static final int REQUEST_CODE = 10;

    protected ListView lvList;
    protected List<Tweet> tweetList;
    protected SwipeRefreshLayout swipeContainer;
    protected TweetAdapter tweetAdapter;
    protected int fragmentId;
    protected ProgressBar pb;
    protected String direction;
    private Tweet tweet;
    private boolean isNewer;
    public TwitterBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(fragmentId, container, false);

        lvList = (ListView) view.findViewById(R.id.lvList);

        tweetList = new ArrayList<>();
        tweetAdapter = new TweetAdapter(getActivity(), tweetList, this);
        lvList.setAdapter(tweetAdapter);
        lvList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getOlderList();

            }
        });
        View footerView = inflater.inflate(R.layout.footer_layout, null, false);
        lvList.addFooterView(footerView);
        pb = (ProgressBar) footerView.findViewById(R.id.pbLoading);
        pb.setVisibility(ProgressBar.INVISIBLE);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewerList();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
         getCachedTweets();
         if(Utils.isNetworkAvailable(getActivity())) {
            getNewerList();

            if (TwitterApplication.getCurrentUser().getUser() == null) {
                TwitterApplication.getCurrentUser().requestCurrentUser();
            }
        }
        return view;
    }

    protected abstract void getCachedTweets();

    protected long currentId = 1;

    protected void getNewerList() {
        currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            currentId = tweetList.get(0).getUid();
        }
       isNewer = true;
    }

    protected void getOlderList() {
        currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            int index = tweetList.size() - 1;
            currentId = tweetList.get(index).getUid();
        }
        pb.setVisibility(View.VISIBLE);
        isNewer = false;
    }


    protected JsonHttpResponseHandler getJsonHttpResponseHandler() {
        return new TwitterJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TwitterJsonHttpResponseHandler.class.getSimpleName(), "succeed " + statusCode);
                updateDataSet(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("statuses");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            updateDataSet(jsonArray);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    protected JsonHttpResponseHandler getActionJsonHttpResponseHandler() {
        return new TwitterJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TwitterJsonHttpResponseHandler.class.getSimpleName(), "succeed " + statusCode);
                Tweet tweetResponse = Tweet.fromJson(response);
                 switch (action){
                    case TweetAdapter.ACTION_FAVORITE:
                        tweet.setFavouritesCount(tweetResponse.getFavouritesCount());
                        tweet.setFavorited(tweetResponse.getFavorited());
                        break;
                    case TweetAdapter.ACTION_RETWEET:
                        tweetList.add(0, tweetResponse);
                        tweet.setRetweeted(true);
                        tweet.setRetweetCount(tweet.getRetweetCount()+1);
                        break;
                }
                tweet.save();
                tweetAdapter.notifyDataSetChanged();
            }
        };
    }

    private void updateDataSet(JSONArray response) {
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        if (pb.getVisibility() == View.VISIBLE) {
            pb.setVisibility(View.INVISIBLE);
        }
        if (response != null && response.length() > 0) {
            ArrayList<Tweet> tweets = Tweet.fromJsonArray(response);
            if(isNewer){
                tweetList.addAll(0, tweets);
            }else {
                tweetList.addAll(tweets);
            }
            tweetAdapter.notifyDataSetChanged();
        }
    }

    
    private String action;
    @Override
    public void onActionClicked(int position, String action) {
        this.tweet = tweetList.get(position);
        this.action = action;
        switch (this.action) {
            case TweetAdapter.ACTION_REPLY:
                launchCompose(tweet.getUid(), tweet.getUser().getScreenName());
                break;
            case TweetAdapter.ACTION_RETWEET:
                TwitterApplication.getRestClient().retweet(tweet.getUid(), getActionJsonHttpResponseHandler());
                break;
            case TweetAdapter.ACTION_FAVORITE:
                if (tweet.getFavorited()) {
                    TwitterApplication.getRestClient().unfavorite(tweet.getUid(), getActionJsonHttpResponseHandler());
                } else {
                    TwitterApplication.getRestClient().favorite(tweet.getUid(), getActionJsonHttpResponseHandler());
                }
                break;
        }
    }


    public void launchCompose(long uid, String screenName) {
        Intent intent = new Intent(getActivity(), ComposeActivity.class);
        intent.putExtra("tweetId", uid);
        intent.putExtra("screenName",  screenName);
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE  && resultCode == Activity.RESULT_OK) {
            Tweet tweet = data.getParcelableExtra("tweet");
            if(tweet != null ) {
               tweetList.add(0, tweet);
                tweetAdapter.notifyDataSetChanged();
            }
        }
    }
}
