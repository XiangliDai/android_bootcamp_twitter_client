package com.codepath.apps.mysimpletweets.fragments;

import android.app.Activity;
import android.net.Uri;
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
import com.codepath.apps.mysimpletweets.adapters.TweetAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MentionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MentionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MentionsFragment extends Fragment {
    private static final int REQUEST_CODE = 10;
    private ListView lvList;
    private List<Tweet> tweetList;
    private SwipeRefreshLayout swipeContainer;
    private TweetAdapter tweetAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MentionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MentionsFragment newInstance() {
        MentionsFragment fragment = new MentionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MentionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mentions, container, false);
        lvList = (ListView) view.findViewById(R.id.lvList);
        tweetList = new ArrayList<>();
        tweetAdapter = new TweetAdapter(getActivity(), tweetList, null);
        lvList.setAdapter(tweetAdapter);
        lvList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getOlderMentions();

            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewerMentions();
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
            getNewerMentions();

            if(TwitterApplication.getCurrentUser().getUser()== null) {
                TwitterApplication.getCurrentUser().requestCurrentUser();
            }
        }
        return view;
    }


    private void getCachedTweets() {
        tweetList.addAll(Tweet.getAll());
        tweetAdapter.notifyDataSetChanged();
    }
    
    
    private void getNewerMentions() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            currentId = tweetList.get(0).getUid();
        }
        TwitterApplication.getRestClient().getNewerMentionsList(currentId, getJsonHttpResponseHandler());
    }

    private void getOlderMentions() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            int index = tweetList.size() - 1;
            currentId = tweetList.get(index).getUid();
        }
        TwitterApplication.getRestClient().getOlderMentionsList(currentId, getJsonHttpResponseHandler());

    }

    private JsonHttpResponseHandler getJsonHttpResponseHandler() {
        return new TwitterJsonHttpResponseHandler(getActivity()) {
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


}
