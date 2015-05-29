package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.ac.korea.ee.fit.R;

/**
 * Created by SHYBook_Air on 15. 5. 28..
 */
public class CollectionFragment extends Fragment {

    public static final String COLLECTION_ID = "COLLECTION ID";

    int collection_id;
    boolean firstTime = true;

    FeedFragment collection;

    TextView collectionTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        collection_id = args.getInt(COLLECTION_ID);

        collection = new FeedFragment();
        Bundle arg = new Bundle();
        arg.putString(FeedFragment.CONTEXT, FeedFragment.COLLECTION);
        arg.putInt(COLLECTION_ID, collection_id);
        collection.setArguments(arg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        collectionTitle = (TextView)view.findViewById(R.id.collectionTitle);
        if (collection_id == 0)
            collectionTitle.setText("좋아하는 패션");
        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (firstTime) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.collectionContainer, collection)
                    .commit();
            firstTime = false;
        }

        return view;
    }
}
