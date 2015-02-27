package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;

public class MentionsFragment extends TwitterBaseFragment {
    private static final int REQUEST_CODE = 10;

    public static MentionsFragment newInstance() {
        MentionsFragment fragment = new MentionsFragment();
        return fragment;
    }

    public MentionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentId = R.layout.fragment_mentions;
    }


    @Override
    protected void getNewerList() {
       super.getNewerList();
        TwitterApplication.getRestClient().getNewerMentionsList(currentId, getJsonHttpResponseHandler());
    }

    @Override
    protected void getOlderList() {
      super.getOlderList();
      TwitterApplication.getRestClient().getOlderMentionsList(currentId, getJsonHttpResponseHandler());
    }

    @Override
    protected void getCachedTweets() {
        //TODO: get all mentions from local DB
    }

}
