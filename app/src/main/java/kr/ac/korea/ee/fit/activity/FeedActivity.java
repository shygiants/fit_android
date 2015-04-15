package kr.ac.korea.ee.fit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import kr.ac.korea.ee.fit.FashionCardAdapter;
import kr.ac.korea.ee.fit.R;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class FeedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        RecyclerView cardList = (RecyclerView) findViewById(R.id.cardList);

        StaggeredGridLayoutManager fashionFeedLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cardList.setLayoutManager(fashionFeedLayoutManager);
        cardList.setAdapter(new FashionCardAdapter());
    }

}
