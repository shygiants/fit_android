package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.korea.ee.fit.R;

/**
 * Created by SHYBook_Air on 15. 5. 19..
 */
public class TabFragment extends Fragment {

    public static final String TAB_CONTENT = "TAB CONTENT";

    public static final String FEED = "FEED";
    public static final String SEARCH = "SEARCH";

    Fragment contentFragment;
    boolean firstTime = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        String tabContent = arguments.getString(TAB_CONTENT);

        switch (tabContent) {
            case FEED:
                contentFragment = new FeedFragment();
                Bundle arg = new Bundle();
                arg.putString(FeedFragment.CONTEXT, FeedFragment.TAB);
                contentFragment.setArguments(arg);
                break;
            case SEARCH:
                contentFragment = new SearchFragment();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        if (firstTime) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.tabContainer, contentFragment)
                    .commit();
            firstTime = false;
        }

        return view;
    }
}
