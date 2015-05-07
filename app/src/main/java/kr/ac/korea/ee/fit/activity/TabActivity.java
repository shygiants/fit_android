package kr.ac.korea.ee.fit.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.TabAdapter;

/**
 * Created by SHY_mini on 15. 5. 8..
 */
public class TabActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        FragmentPagerAdapter adapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}

