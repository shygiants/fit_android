package kr.ac.korea.ee.fit.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.fragment.FeedFragment;
import kr.ac.korea.ee.fit.fragment.SearchFragment;

/**
 * Created by SHY_mini on 15. 5. 8..
 */
public class TabActivity extends FragmentActivity {
    
    FragmentTabHost tabHost;

    ArrayList<Fragment> fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        
        tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
//        tabHost.getTabWidget().setStripEnabled(false);
        tabHost.getTabWidget().setBackgroundColor(0xFFE0E0E0);

        // TODO: fill tab content with icon or something
        tabHost.addTab(tabHost.newTabSpec("feed").setIndicator("Feed"), FeedFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("search").setIndicator("Search"), SearchFragment.class, null);
    }

    @Override
    public void onBackPressed() {
        FragmentManager tabFragmentManager = getSupportFragmentManager();
        if (tabFragmentManager.getBackStackEntryCount() > 0)
            tabFragmentManager.popBackStack();
        else
            finish();
    }
}

