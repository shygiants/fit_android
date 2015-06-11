package kr.ac.korea.ee.fit.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.request.CollectionData;
import kr.ac.korea.ee.fit.request.Event;

/**
 * Created by SHYBook_Air on 15. 5. 28..
 */
public class CollectionFragment extends Fragment implements View.OnClickListener {

    public static final String COLLECTION_ID = "COLLECTION ID";
    public static final String USER_ID = "USER ID";
    public static final String NAME = "NAME";
    public static final String DESC = "DESCRIPTION";

    int collection_id;
    String user_id;
    String name;
    String desc;
    boolean firstTime = true;
    boolean isLiked = false;

    FeedFragment collection;

    TextView collectionTitle;
    ImageButton likeButton;

    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("진행중...");
        dialog.setTitle("네트워크 체크");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        Bundle args = getArguments();
        collection_id = args.getInt(COLLECTION_ID);
        user_id = args.getString(USER_ID);
        name = args.getString(NAME);
        desc = args.getString(DESC);

        new IsLiked().start(CollectionData.isLiked(collection_id));

        collection = new FeedFragment();
        Bundle arg = new Bundle();
        arg.putString(FeedFragment.CONTEXT, FeedFragment.COLLECTION);
        arg.putInt(COLLECTION_ID, collection_id);
        arg.putString(USER_ID, user_id);

        collection.setArguments(arg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        collectionTitle = (TextView)view.findViewById(R.id.collectionTitle);
        collectionTitle.setText(name);

        likeButton = (ImageButton)view.findViewById(R.id.likeButton);
        if (collection_id == 0)
            likeButton.setVisibility(View.GONE);
        else {
            likeButton.setOnClickListener(this);
            likeButton.setSelected(isLiked);
        }

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

    @Override
    public void onClick(View v) {
        // TODO: like collection
        dialog.show();
        new LikeCollection().start(Event.likeCollection(collection_id));
    }

    private class LikeCollection extends HTTPClient<Event> {

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                likeButton.setSelected(result.getBoolean("like"));
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class IsLiked extends HTTPClient<CollectionData> {

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                isLiked = result.getBoolean("isLiked");
                likeButton.setSelected(isLiked);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
