package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MentionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MentionsFragment extends TwitterBaseFragment {
    private static final int REQUEST_CODE = 10;

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
        this.fragmentId = R.layout.fragment_mentions;
    }


    @Override
    protected void getNewerList() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            currentId = tweetList.get(0).getUid();
        }
        TwitterApplication.getRestClient().getNewerMentionsList(currentId, getJsonHttpResponseHandler());
    }

    @Override
    protected void getOlderList() {
        long currentId = 1;
        if (tweetList != null && tweetList.size() > 0) {
            int index = tweetList.size() - 1;
            currentId = tweetList.get(index).getUid();
        }
        TwitterApplication.getRestClient().getOlderMentionsList(currentId, getJsonHttpResponseHandler());
    }



    @Override
    protected void getCachedTweets() {

    }

}
