package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.net.TwitterClient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    public static final String ACTION_REPLY = "reply";
    public static final String ACTION_RETWEET = "retweet";
    public static final String ACTION_FAVORITE = "favorite";
private Context context;

    public interface IActionClickListener{
        void onActionClicked(int position, String action);
    }
    
    private IActionClickListener actionClickListener;
    private static class ViewHolder {
        public ImageView ivImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvCreatedTime;
        public TextView tvScreenName;
        public TextView tvRetweets;
        public TextView tvlikes;
        public TextView tvRetweeted;
        public ImageView ivReply;
    }

    public TweetAdapter(Context context, List<Tweet> tweets,  IActionClickListener actionClickListener) {
        super(context, 0, tweets);
        this.context = context;
        this.actionClickListener = actionClickListener;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);
            viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tvCreatedTime = (TextView)convertView.findViewById(R.id.tvCreatedTime);
            viewHolder.tvScreenName = (TextView)convertView.findViewById(R.id.tvScreeName);
            viewHolder.tvlikes = (TextView)convertView.findViewById(R.id.tvlikes);
            viewHolder.tvRetweets = (TextView)convertView.findViewById(R.id.tvRetweets);
            viewHolder.tvRetweeted = (TextView)convertView.findViewById(R.id.tvRetweeted);
            viewHolder.ivReply = (ImageView)convertView.findViewById(R.id.ivReply);
            convertView.setTag(viewHolder);
            
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvBody.setText(Html.fromHtml(tweet.getBody()));
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvScreenName.setText(tweet.getUser().getScreenName());
        viewHolder.tvCreatedTime.setText(tweet.getCreatedAt());
        if(tweet.getRetweenName() != null && !tweet.getRetweenName().isEmpty()){
            viewHolder.tvRetweeted.setText(tweet.getRetweenName() + " retweeted");
            viewHolder.tvRetweeted.setVisibility(View.VISIBLE);
        
           
        }
        else { 
            viewHolder.tvRetweeted.setVisibility(View.GONE);

        }
        if(tweet.getFavouritesCount() > 0) {
            viewHolder.tvlikes.setText(tweet.getFavouritesCount() + "");
        }else{
            viewHolder.tvlikes.setText("");
        }
        
        if(tweet.getRetweetCount() > 0) {
            viewHolder.tvRetweets.setText(tweet.getRetweetCount() + "");
        }else{
            viewHolder.tvRetweets.setText("");
        }
        
        viewHolder.ivImage.setImageResource(0);

        Picasso.with(getContext())
                .load(Uri.parse(tweet.getUser().getProfileImageUrl()))
                .fit().centerCrop()
                .into(viewHolder.ivImage);
        
        viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    launchProfile(tweet.getUser().getUid());
            }
        });

        viewHolder.ivReply.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(actionClickListener!=null)
                            actionClickListener.onActionClicked(position, ACTION_REPLY);
                    }
                }
        );
        
        viewHolder.tvlikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actionClickListener!=null)
                    actionClickListener.onActionClicked(position, ACTION_FAVORITE);

            }
        });
        viewHolder.tvRetweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actionClickListener!=null)
                    actionClickListener.onActionClicked(position, ACTION_RETWEET);
            }
        });
        return convertView;
    }

    private void launchProfile(Long userId) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("userId", userId == 0 ? TwitterClient.CURRENT_USER_ID : userId);
        getContext().startActivity(intent);
    }

}
