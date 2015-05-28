package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.model.User;

/**
 * Created by SHYBook_Air on 15. 5. 28..
 */
public class UserFragment extends Fragment {

    TextView nickNameText;
    TextView nameText;

    TextView ratingText;
    TextView followerText;
    TextView followingText;

    User user;

    CollectionAdapter collectionAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.get();
        collectionAdapter = new CollectionAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        nickNameText = (TextView)view.findViewById(R.id.nickName);
        nickNameText.setText(user.getNickName());
        nameText = (TextView)view.findViewById(R.id.name);
        nameText.setText(user.getName());

        ratingText = (TextView)view.findViewById(R.id.rating);
        ratingText.setText(String.valueOf(user.getRating()));
        followerText = (TextView)view.findViewById(R.id.follower);
        followerText.setText(String.valueOf(user.getFollower()));
        followingText = (TextView)view.findViewById(R.id.following);
        followingText.setText(String.valueOf(user.getFollowing()));

        RecyclerView collectionList = (RecyclerView)view.findViewById(R.id.collectionList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);

        collectionList.setLayoutManager(gridLayoutManager);
        collectionList.setAdapter(collectionAdapter);

        return view;
    }

    private class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionHolder> {

        public class CollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView thumbnail;
            TextView collectionTitle;

            public CollectionHolder(View view) {
                super(view);

                thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
                collectionTitle = (TextView)view.findViewById(R.id.collectionTitle);

                view.setOnClickListener(this);
            }

            public void setView() {

            }

            @Override
            public void onClick(View v) {
                CollectionFragment collectionFragment = new CollectionFragment();
                Bundle arg = new Bundle();
                arg.putInt(CollectionFragment.COLLECTION_ID, 0);
                collectionFragment.setArguments(arg);

                getFragmentManager().beginTransaction()
                        .replace(R.id.tabContainer, collectionFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        @Override
        public void onBindViewHolder(CollectionHolder holder, int position) {

        }

        @Override
        public CollectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_collection, parent, false);

            return new CollectionHolder(view);
        }
    }
}
