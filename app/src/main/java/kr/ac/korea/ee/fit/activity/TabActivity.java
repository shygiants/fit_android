package kr.ac.korea.ee.fit.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.fragment.FeedFragment;
import kr.ac.korea.ee.fit.fragment.SearchFragment;
import kr.ac.korea.ee.fit.fragment.TabFragment;

/**
 * Created by SHY_mini on 15. 5. 8..
 */
public class TabActivity extends FragmentActivity {
    
    FragmentTabHost tabHost;

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
        for (int i = 0; i < count; i++)
            tabWidget.getChildAt(i).getLayoutParams().height *= 4.0 / 5.0;
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

