package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.DeviceDimensionsHelper;
import com.codepath.apps.mysimpletweets.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterJsonHttpResponseHandler;
import com.codepath.apps.mysimpletweets.adapters.TweetAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    RelativeLayout rlProfile;
    TextView tvUserName;
    TextView tvScreenName;
    ImageView ivProfile;
    TextView tvTweetsCount;
    TextView tvFollowingCount;
    TextView tvFollowerCount;
    ListView lvList;
    ImageView ivProfileBackground;
    private SwipeRefreshLayout swipeContainer;
    private List<Tweet> tweetList;
    private TweetAdapter tweetAdapter;
    private Long userId;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *    
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(Long userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.userId =  getArguments().getLong("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
       tvUserName = (TextView) view.findViewById(R.id.tvUserName) ; 
       tvScreenName = (TextView) view.findViewById(R.id.tvScreeName) ;
       ivProfile = (ImageView) view.findViewById(R.id.ivProfile) ;
       tvTweetsCount = (TextView) view.findViewById(R.id.tvTweetsCount) ;
       tvFollowingCount = (TextView) view.findViewById(R.id.tvFollowingCount) ;
       tvFollowerCount = (TextView) view.findViewById(R.id.tvFollowerCount) ;
       lvList = (ListView) view.findViewById(R.id.lvList) ;
        ivProfileBackground = (ImageView) view.findViewById(R.id.ivProfileBackground);
        tweetList = new ArrayList<>();
        tweetAdapter = new TweetAdapter(getActivity(), tweetList, null);
        lvList.setAdapter(tweetAdapter);
        lvList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getOlderuserTimeline();

            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewerUserTimeline();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getUser();
        getNewerUserTimeline();



        return view;
    }

    private void getUser() {
        TwitterApplication.getRestClient().getUserInformation(userId, getUserJsonHttpResponseHandler());

    }


    private void getNewerUserTimeline() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0 ) {
            currentId = tweetList.get(0).getUid();
        }
        TwitterApplication.getRestClient().getNewerUserTimelineList(userId, currentId, getJsonHttpResponseHandler());
    }

    private void getOlderuserTimeline() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0 ) {
            int index = tweetList.size() - 1;
            currentId = tweetList.get(index).getUid();
        }
        TwitterApplication.getRestClient().getOlderUserTimelineList(userId, currentId, getJsonHttpResponseHandler());

    }


    private JsonHttpResponseHandler getUserJsonHttpResponseHandler() {
        return new TwitterJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {             
                if (response != null) {
                    User user = User.fromJson(response);
                    
                    tvUserName.setText(user.getName());
                    tvScreenName.setText(user.getScreenName());
                    tvFollowerCount.setText(user.getFollowersCount()+"");
                    tvFollowingCount.setText(user.getFriendsCount()+"");
                    tvTweetsCount.setText(user.getStatusesCount()+"");

                    int deviceWidth = DeviceDimensionsHelper.getDisplayWidth(getActivity());
                    if(!user.getProfileBackgroundImageUrl().isEmpty()) {
                        Picasso.with(getActivity()).load(user.getProfileBackgroundImageUrl()).resize(deviceWidth, 0).into(ivProfileBackground);
                    }
                    Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivProfile);

                    
                }
            }
        };
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
