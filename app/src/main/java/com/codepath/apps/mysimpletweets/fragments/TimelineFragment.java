package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends TwitterBaseFragment implements TweetAdapter.IProfileImageClickListener {
    private static final int REQUEST_CODE = 10;

    

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
      
        
        fragment.setArguments(args);
        return fragment;
    }

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
         
        }
        this.fragmentId = R.layout.fragment_timeline;
    }

    @Override
    protected void getNewerList() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            currentId = tweetList.get(0).getUid();
        }
        TwitterApplication.getRestClient().getNewerTimelineList(currentId, getJsonHttpResponseHandler());
    }

    @Override
    protected void getOlderList() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            int index = tweetList.size() - 1;
            currentId = tweetList.get(index).getUid();
        }
        TwitterApplication.getRestClient().getOlderTimelineList(currentId, getJsonHttpResponseHandler());
    }


    @Override
    protected void getCachedTweets() {
        tweetList.addAll(Tweet.getAll());
        tweetAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClicked(Long userId) {

        launchProfile(userId);
    }

    private void launchProfile(Long userId) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("userId", userId == 0 ?  TwitterClient.CURRENT_USER_ID :userId);
        startActivityForResult(intent, REQUEST_CODE);
    }

}
