package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineFragment extends TwitterBaseFragment{

    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
       
        return fragment;
    }

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
        this.fragmentId = R.layout.fragment_timeline;
    }

    @Override
    protected void getNewerList() {
        super.getNewerList();
        TwitterApplication.getRestClient().getNewerTimelineList(currentId, getJsonHttpResponseHandler());
    }

    @Override
    protected void getOlderList() {
      super.getOlderList();
      TwitterApplication.getRestClient().getOlderTimelineList(currentId, getJsonHttpResponseHandler());
    }
    
    @Override
    protected void getCachedTweets() {
        tweetList.addAll(Tweet.getAll());
        tweetAdapter.notifyDataSetChanged();
    }

}
