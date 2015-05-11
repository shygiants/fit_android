package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.korea.ee.fit.R;

/**
 * Created by SHY_mini on 15. 5. 8..
 */
public class TabFragment extends android.support.v4.app.Fragment {

    public static final String ARG = "Argument";
    public static final String FRAGMENT_MANAGER = "Fragment manager";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        switch (getArguments().getInt(ARG)) {
            case 0:
                view = inflater.inflate(R.layout.fragment_tab, container, false);
                getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.tab, new FeedFragment())
                    .commit();
                return view;
            case 1:
                view = inflater.inflate(R.layout.activity_signin, container, false);
                return view;
            case 2:
                view = inflater.inflate(R.layout.activity_signup, container, false);
                return view;
            default:
                return null;

        }
    }
}
