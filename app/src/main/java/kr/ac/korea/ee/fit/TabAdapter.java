package kr.ac.korea.ee.fit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by SHY_mini on 15. 5. 8..
 */
public class TabAdapter extends FragmentPagerAdapter {

    static final int NUM_TABS = 3;

    public TabAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment tab = new TabFragment();
        Bundle arg = new Bundle();
        arg.putInt(TabFragment.ARG, position);
        tab.setArguments(arg);
        return tab;
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + (position + 1);
    }
}
