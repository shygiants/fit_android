package kr.ac.korea.ee.fit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.fragment.DetailFragment;
import kr.ac.korea.ee.fit.fragment.FeedFragment;
import kr.ac.korea.ee.fit.fragment.SearchFragment;
import kr.ac.korea.ee.fit.fragment.TabFragment;
import kr.ac.korea.ee.fit.fragment.UserFragment;

/**
 * Created by SHY_mini on 15. 5. 8..
 */
public class TabActivity extends FragmentActivity implements View.OnClickListener {
    
    FragmentTabHost tabHost;
    int currentTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        
        tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        TabWidget tabWidget = tabHost.getTabWidget();
        tabWidget.setBackgroundColor(getResources().getColor(R.color.app_bar));
        tabWidget.setDividerDrawable(null);

        Bundle arg_feed = new Bundle();
        arg_feed.putString(TabFragment.TAB_CONTENT, TabFragment.FEED);
        Bundle arg_search = new Bundle();
        arg_search.putString(TabFragment.TAB_CONTENT, TabFragment.SEARCH);
        Bundle arg_user = new Bundle();
        arg_user.putString(TabFragment.TAB_CONTENT, TabFragment.USER);

        tabHost.addTab(tabHost.newTabSpec("feed").setIndicator("", getDrawable(R.drawable.tab_feed)), TabFragment.class, arg_feed);
        tabHost.addTab(tabHost.newTabSpec("search").setIndicator("", getDrawable(R.drawable.tab_search)), TabFragment.class, arg_search);
        tabHost.addTab(tabHost.newTabSpec("user").setIndicator("", getDrawable(R.drawable.tab_user)), TabFragment.class, arg_user);

        int count = tabWidget.getChildCount();
        for (int i = 0; i < count; i++) {
            tabWidget.getChildAt(i).getLayoutParams().height *= 4.0 / 5.0;
            tabWidget.getChildAt(i).setOnClickListener(this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment target;
        switch (requestCode) {
            case UserFragment.PROFILE:
            case UserFragment.COLLECTION:
                target = getSupportFragmentManager().findFragmentByTag(tabHost.getCurrentTabTag()).getChildFragmentManager().findFragmentByTag(TabFragment.USER);
                target.onActivityResult(requestCode, resultCode, data);
                break;
            case DetailFragment.COLLECTION:
                FeedFragment feedFragment = (FeedFragment)getSupportFragmentManager().findFragmentByTag(tabHost.getCurrentTabTag()).getChildFragmentManager().findFragmentByTag(TabFragment.FEED);
                target = feedFragment.fragmentManager.findFragmentByTag(FeedFragment.DETAIL);
                target.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    @Override
    public void onClick(View view) {
        TabWidget tabWidget = tabHost.getTabWidget();
        int count = tabWidget.getChildCount();
        for (int i = 0; i < count; i++)
            if (tabWidget.getChildAt(i) == view) {
                // click for reset
                if (i == currentTab)
                    clearTab();
                // switch tab
                else {
                    tabHost.setCurrentTab(i);
                    currentTab = i;
                }
                break;
            }
    }

    void clearTab() {
        FragmentManager tabFragmentManager = getSupportFragmentManager().findFragmentByTag(tabHost.getCurrentTabTag()).getChildFragmentManager();

        while (tabFragmentManager.getBackStackEntryCount() > 0)
            tabFragmentManager.popBackStackImmediate();
    }

    @Override
    public void onBackPressed() {
        FragmentManager tabFragmentManager = getSupportFragmentManager().findFragmentByTag(tabHost.getCurrentTabTag()).getChildFragmentManager();
        if (tabFragmentManager.getBackStackEntryCount() > 0)
            tabFragmentManager.popBackStack();
        else
            finish();
    }
}

