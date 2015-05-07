package kr.ac.korea.ee.fit;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SHY_mini on 15. 5. 8..
 */
public class TabFragment extends android.support.v4.app.Fragment {

    public static final String ARG = "Argument";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        switch (getArguments().getInt(ARG)) {
            case 0:
                view = inflater.inflate(R.layout.activity_feed, container, false);

                RecyclerView cardList = (RecyclerView)view.findViewById(R.id.cardList);

                Configuration config = getResources().getConfiguration();
                boolean isLarge = (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                        Configuration.SCREENLAYOUT_SIZE_LARGE;

                StaggeredGridLayoutManager fashionFeedLayoutManager =
                        new StaggeredGridLayoutManager((isLarge)? 3 : 2, StaggeredGridLayoutManager.VERTICAL);

                cardList.setLayoutManager(fashionFeedLayoutManager);
                cardList.setAdapter(new FashionCardAdapter());

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
