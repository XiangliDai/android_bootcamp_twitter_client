package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.MentionsFragment;
import com.codepath.apps.mysimpletweets.fragments.TimelineFragment;

public class TwitterFragmentPagerAdapter extends FragmentPagerAdapter{
        final int PAGE_COUNT = 2;
        private String tabTitles[] ;
        private Context context;
        private Fragment timeLineFragment;
        private Fragment mentionFragment;
                
        public TwitterFragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
            timeLineFragment = TimelineFragment.newInstance();
            mentionFragment = MentionsFragment.newInstance();
            tabTitles = new String[] { this.context.getString(R.string.home) , this.context.getString(R.string.mention)};
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return timeLineFragment;
                case 1:
                    return mentionFragment;
            }
            return timeLineFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

}
