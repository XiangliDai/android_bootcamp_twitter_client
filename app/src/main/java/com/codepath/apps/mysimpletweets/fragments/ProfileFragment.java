package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.DeviceDimensionsHelper;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterJsonHttpResponseHandler;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileFragment extends TwitterBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    RelativeLayout rlProfile;
    TextView tvUserName;
    TextView tvScreenName;
    ImageView ivProfile;
    TextView tvTweetsCount;
    TextView tvFollowingCount;
    TextView tvFollowerCount;
    ImageView ivProfileBackground;
   
    private Long userId;
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
            this.userId = getArguments().getLong("userId");
        }
        this.fragmentId = R.layout.fragment_profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = super.onCreateView(inflater, container, savedInstanceState);
       tvUserName = (TextView) view.findViewById(R.id.tvUserName) ; 
       tvScreenName = (TextView) view.findViewById(R.id.tvScreeName) ;
       ivProfile = (ImageView) view.findViewById(R.id.ivProfile) ;
       tvTweetsCount = (TextView) view.findViewById(R.id.tvTweetsCount) ;
       tvFollowingCount = (TextView) view.findViewById(R.id.tvFollowingCount) ;
       tvFollowerCount = (TextView) view.findViewById(R.id.tvFollowerCount) ;
       ivProfileBackground = (ImageView) view.findViewById(R.id.ivProfileBackground);

        getUser();

        return view;
    }

    @Override
    protected void getCachedTweets() {
        //TODO: get all user times from local DB
    }

    @Override
    protected void getNewerList() {
      super.getNewerList();
      TwitterApplication.getRestClient().getNewerUserTimelineList(userId, currentId, getJsonHttpResponseHandler());
    }

    @Override
    protected void getOlderList() {
      super.getOlderList();
      TwitterApplication.getRestClient().getOlderUserTimelineList(userId, currentId, getJsonHttpResponseHandler());

    }

    private void getUser() {
        TwitterApplication.getRestClient().getUserInformation(userId, getUserJsonHttpResponseHandler());
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
                    if(user.getProfileBackgroundImageUrl()!= null && !user.getProfileBackgroundImageUrl().isEmpty()) {
                        Picasso.with(getActivity()).load(user.getProfileBackgroundImageUrl()).resize(deviceWidth, 0).into(ivProfileBackground);
                    }
                    Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivProfile);
                }
            }
        };
    }
    
    @Override
    public void onClicked(Long userId) {
       return;
    }

   
}
