package com.codepath.apps.mysimpletweets.fragments;

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
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
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


public abstract class TwitterBaseFragment extends Fragment implements TweetAdapter.IProfileImageClickListener {
    protected ListView lvList;
    protected List<Tweet> tweetList;
    protected SwipeRefreshLayout swipeContainer;
    protected TweetAdapter tweetAdapter;
    protected int fragmentId;
    protected ProgressBar pb;
    public TwitterBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(fragmentId, container, false);

        lvList = (ListView) view.findViewById(R.id.lvList) ;

        tweetList = new ArrayList<>();
        tweetAdapter = new TweetAdapter(getActivity(), tweetList, this);
        lvList.setAdapter(tweetAdapter);
        lvList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getOlderList();

            }
        });
        View footerView =  inflater.inflate(R.layout.footer_layout, null, false);
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
        if(!Utils.isNetworkAvailable(getActivity())){
            getCachedTweets();
        }
        else {
            getNewerList();

            if(TwitterApplication.getCurrentUser().getUser()== null) {
                TwitterApplication.getCurrentUser().requestCurrentUser();
            }
        }
        return view;
    }

    protected abstract void getCachedTweets() ;
    protected long currentId = 1;
    protected void getNewerList(){
        currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            currentId = tweetList.get(0).getUid();
        }
        
    }

    protected void getOlderList(){
        currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            int index = tweetList.size() - 1;
            currentId = tweetList.get(index).getUid();
        }
        pb.setVisibility(View.VISIBLE);
        
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
                        if(jsonArray != null && jsonArray.length() >0){
                            updateDataSet(jsonArray);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void updateDataSet(JSONArray response) {
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        if(pb.getVisibility() == View.VISIBLE){
            pb.setVisibility(View.INVISIBLE);
        }
        if (response != null && response.length() > 0) {
            tweetList.addAll(Tweet.fromJsonArray(response));
            tweetAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClicked(Long userId) {
        launchProfile(userId);
    }

    private void launchProfile(Long userId) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("userId", userId == 0 ?  TwitterClient.CURRENT_USER_ID :userId);
        startActivity(intent);
    }

}
