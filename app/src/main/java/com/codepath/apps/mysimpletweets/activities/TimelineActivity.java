package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TwitterFragmentPagerAdapter;
import com.codepath.apps.mysimpletweets.fragments.TwitterBaseFragment;
import com.codepath.apps.mysimpletweets.net.TwitterClient;

public class TimelineActivity extends ActionBarActivity {
    private static final String TAG = TimelineActivity.class.getSimpleName();
     private PagerSlidingTabStrip tabsStrip;
    private ViewPager fragmentViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.mipmap.ic_launcher);
        
        getSupportActionBar().setTitle("");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        fragmentViewPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentViewPager.setAdapter(new TwitterFragmentPagerAdapter(this,  getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(fragmentViewPager);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && query.length() > 0) {
                    // added to minimize background color flicker navigating to inbox search fragment
                    launchSearchActivity(query);
                    //clear query
                    searchView.setQuery("", false);
                    searchMenuItem.collapseActionView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void launchSearchActivity(String query) {
        Intent intent = new Intent(this, SearchActivity.class);

        intent.putExtra("query", query);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
            launchComposer();
            return true;
        }
        if (id == R.id.action_profile) {
            launchProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId",  TwitterClient.CURRENT_USER_ID );
        startActivity(intent);
    }

    private void launchComposer() {
        int index = fragmentViewPager.getCurrentItem();
        TwitterBaseFragment fragment = (TwitterBaseFragment)((TwitterFragmentPagerAdapter)fragmentViewPager.getAdapter()).getItem(index);

        fragment.launchCompose(0, "");
    }
}
