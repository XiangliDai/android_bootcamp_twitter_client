package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        public ImageView ivImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvCreatedTime;
        public TextView tvScreenName;
        public TextView tvRetweets;
        public TextView tvlikes;
    }

    public TweetAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
            convertView.setTag(viewHolder);
            
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvBody.setText(Html.fromHtml(tweet.getBody()));
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvScreenName.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvCreatedTime.setText(tweet.getCreatedAt());
        if(tweet.getFavouritesCount() > 0) {
            viewHolder.tvlikes.setText(tweet.getFavouritesCount() + "");
        }
        
        if(tweet.getRetweetCount() > 0) {
            viewHolder.tvRetweets.setText(tweet.getRetweetCount() + "");
        }
        viewHolder.ivImage.setImageResource(0);

        Picasso.with(getContext())
                .load(Uri.parse(tweet.getUser().getProfileImageUrl()))
                .fit().centerCrop()
                .into(viewHolder.ivImage);
        return convertView;
    }

}
