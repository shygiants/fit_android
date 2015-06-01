package kr.ac.korea.ee.fit.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.Event;
import kr.ac.korea.ee.fit.request.GetUserData;

/**
 * Created by SHYBook_Air on 15. 5. 28..
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    public static final String USER_ID = "USER ID";

    User user;
    boolean isDeviceUser;

    TextView nickNameText;
    TextView nameText;

    TextView ratingText;
    TextView followerText;
    TextView followingText;

    Button followOrEdit;

    RecyclerView collectionList;
    CollectionAdapter collectionAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arg = getArguments();
        String userId = arg.getString(USER_ID);

        if (isDeviceUser = User.isDeviceUser(userId)) {
            user = User.getDeviceUser();
            collectionAdapter = new CollectionAdapter();
        }
        else {
            UserGetterTask getter = new UserGetterTask();
            getter.start(new GetUserData(userId));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        nickNameText = (TextView)view.findViewById(R.id.nickName);
        nameText = (TextView)view.findViewById(R.id.name);
        ratingText = (TextView)view.findViewById(R.id.rating);
        followerText = (TextView)view.findViewById(R.id.follower);
        followingText = (TextView)view.findViewById(R.id.following);
        followOrEdit = (Button)view.findViewById(R.id.followOrEdit);

        followOrEdit.setOnClickListener(this);

        collectionList = (RecyclerView)view.findViewById(R.id.collectionList);
        Configuration config = getResources().getConfiguration();
        boolean isLarge = (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), (isLarge)? 3 : 2, LinearLayoutManager.VERTICAL, false);

        collectionList.setLayoutManager(gridLayoutManager);

        if (isDeviceUser || user != null) {
            nickNameText.setText(user.getNickName());
            nameText.setText(user.getName());
            ratingText.setText(String.valueOf(user.getRating()));
            followerText.setText(String.valueOf(user.getFollower()));
            followingText.setText(String.valueOf(user.getFollowing()));
            collectionList.setAdapter(collectionAdapter);

            if (isDeviceUser) {
                followOrEdit.setSelected(false);
                followOrEdit.setText("프로필 편집");
                followOrEdit.setTextColor(getResources().getColor(R.color.accent));
            }
            else {
                boolean isFollowing = user.isFollowing();
                followOrEdit.setSelected(isFollowing);
                followOrEdit.setText((isFollowing) ? "팔로잉" : "+ 팔로우");
                followOrEdit.setTextColor(getResources().getColor((isFollowing)? R.color.icons : R.color.accent));
            }
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if (!isDeviceUser) {
            FollowTask followTask = new FollowTask();
            followTask.start(Event.follow(user.getEmail()));
        }
    }

    private class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionHolder> {

        public class CollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView thumbnail;
            TextView collectionTitle;

            public CollectionHolder(View view) {
                super(view);

                thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
                collectionTitle = (TextView)view.findViewById(R.id.collectionTitle);

                collectionTitle.setText("좋아하는 패션");

                view.setOnClickListener(this);
            }

            public void setView() {

            }

            @Override
            public void onClick(View v) {
                CollectionFragment collectionFragment = new CollectionFragment();
                Bundle arg = new Bundle();
                arg.putInt(CollectionFragment.COLLECTION_ID, 0);
                arg.putString(CollectionFragment.USER_ID, user.getEmail());
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

    private class UserGetterTask extends HTTPClient<GetUserData> {
        @Override
        protected void onPostExecute(JSONObject result) {
            user = new User(result);
            nickNameText.setText(user.getNickName());
            nameText.setText(user.getName());
            ratingText.setText(String.valueOf(user.getRating()));
            followerText.setText(String.valueOf(user.getFollower()));
            followingText.setText(String.valueOf(user.getFollowing()));
            collectionAdapter = new CollectionAdapter();
            collectionList.setAdapter(collectionAdapter);

            boolean isFollowing = user.isFollowing();
            followOrEdit.setSelected(isFollowing);
            followOrEdit.setText((isFollowing) ? "팔로잉" : "+ 팔로우");
            followOrEdit.setTextColor(getResources().getColor((isFollowing) ? R.color.icons : R.color.accent));
        }
    }

    private class FollowTask extends HTTPClient<Event> {

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                boolean isFollowing = result.getBoolean("is_following");
                followOrEdit.setSelected(isFollowing);
                followOrEdit.setText((isFollowing) ? "팔로잉" : "+ 팔로우");
                followOrEdit.setTextColor(getResources().getColor((isFollowing)? R.color.icons : R.color.accent));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
