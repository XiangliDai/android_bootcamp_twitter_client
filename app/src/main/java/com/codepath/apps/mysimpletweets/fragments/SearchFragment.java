package com.codepath.apps.mysimpletweets.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends TwitterBaseFragment {

    private String query;
    public static SearchFragment newInstance(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.query = getArguments().getString("query");
        }
        this.fragmentId = R.layout.fragment_search;
    }

    @Override
    protected void getCachedTweets() {

    }
    
    @Override
    protected void getNewerList() {
        super.getNewerList();
        TwitterApplication.getRestClient().getNewerSearchList(query, currentId, getJsonHttpResponseHandler());
    }

    @Override
    protected void getOlderList() {
        super.getOlderList();
        TwitterApplication.getRestClient().getOlderSearchList(query, currentId, getJsonHttpResponseHandler());
    }



}
