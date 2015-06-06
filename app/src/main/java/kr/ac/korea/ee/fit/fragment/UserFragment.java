package kr.ac.korea.ee.fit.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import kr.ac.korea.ee.fit.Authenticator;
import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.activity.CollectionActivity;
import kr.ac.korea.ee.fit.activity.MainActivity;
import kr.ac.korea.ee.fit.activity.ProfileActivity;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.core.MyGridLayoutManager;
import kr.ac.korea.ee.fit.model.Collection;
import kr.ac.korea.ee.fit.model.FashionCard;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.CollectionData;
import kr.ac.korea.ee.fit.request.Event;
import kr.ac.korea.ee.fit.request.UserData;

/**
 * Created by SHYBook_Air on 15. 5. 28..
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    public static final String USER_ID = "USER ID";
    public static final String NICKNAME = "NICKNAME";
    public static final String NAME = "NAME";
    public static final String DESC = "DESC";
    public static final int PROFILE = 1;
    public static final int COLLECTION = 2;

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

    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arg = getArguments();
        String userId = arg.getString(USER_ID);

        if (isDeviceUser = User.isDeviceUser(userId)) {
            user = User.getDeviceUser();
            collectionAdapter = new CollectionAdapter();
            GetCollections getCollections = new GetCollections();
            getCollections.start(CollectionData.getCollections(user.getEmail()));
        }
        else {
            UserGetterTask getter = new UserGetterTask();
            getter.start(UserData.getUserData(userId));
        }

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("진행중...");
        dialog.setTitle("네트워크 체크");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
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
        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(getActivity(), (isLarge)? 3 : 2, LinearLayoutManager.VERTICAL, false);
        collectionList.setLayoutManager(gridLayoutManager);

        if (isDeviceUser || user != null) {
            setView();
            collectionList.setAdapter(collectionAdapter);

            if (isDeviceUser) {
                followOrEdit.setSelected(false);
                followOrEdit.setText("설정");
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

    public void setView() {
        nickNameText.setText(user.getNickName());
        nameText.setText(user.getName());
        ratingText.setText(String.valueOf(user.getRating()));
        followerText.setText(String.valueOf(user.getFollower()));
        followingText.setText(String.valueOf(user.getFollowing()));
    }

    @Override
    public void onClick(View view) {
        if (!isDeviceUser) {
            dialog.show();
            FollowTask followTask = new FollowTask();
            followTask.start(Event.follow(user.getEmail()));
        }
        else {
            new BottomSheet.Builder(getActivity()).title("설정").sheet(R.menu.bottom_sheet)
                    .listener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case R.id.profile:
                                    Intent editProfile = new Intent(getActivity(), ProfileActivity.class);
                                    getActivity().startActivityForResult(editProfile, PROFILE);
                                    break;
                                case R.id.logout:
                                    Authenticator.deleteAccount(getActivity());
                                    Intent logout = new Intent(getActivity(), MainActivity.class);
                                    startActivity(logout);
                                    getActivity().finish();
                                    break;
                            }
                        }
            }).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case PROFILE:
                    dialog.show();
                    String nickName = data.getStringExtra(NICKNAME);
                    UpdateNickName updateNickName = new UpdateNickName(nickName);
                    updateNickName.start(UserData.updateNickName(nickName));
                    break;
                case COLLECTION:
                    dialog.show();
                    String name = data.getStringExtra(NAME);
                    String description = data.getStringExtra(DESC);
                    MakeCollection makeCollection = new MakeCollection(name);
                    makeCollection.start(CollectionData.makeCollection(name, description));
                    break;
            }
    }



    private class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public class CollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView thumbnail;
            TextView collectionTitle;
            Collection collection;

            public CollectionHolder(View view) {
                super(view);

                thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
                collectionTitle = (TextView)view.findViewById(R.id.collectionTitle);

                view.setOnClickListener(this);
            }

            public void setView(Collection collection) {
                this.collection = collection;
                collectionTitle.setText(collection.getCollectionName());
                Bitmap tn = collection.getThumbnail();
                if (tn != null)
                    thumbnail.setImageBitmap(tn);
                else
                    thumbnail.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            }

            @Override
            public void onClick(View v) {
                CollectionFragment collectionFragment = new CollectionFragment();
                Bundle arg = new Bundle();
                arg.putInt(CollectionFragment.COLLECTION_ID, collection.getCollectionId());
                arg.putString(CollectionFragment.USER_ID, user.getEmail());
                arg.putString(CollectionFragment.NAME, collection.getCollectionName());
                arg.putString(CollectionFragment.DESC, collection.getCollectionDesc());
                collectionFragment.setArguments(arg);

                getFragmentManager().beginTransaction()
                        .replace(R.id.tabContainer, collectionFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        public class AddCollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public AddCollectionHolder(View view) {
                super(view);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent makeCollection = new Intent(getActivity(), CollectionActivity.class);
                getActivity().startActivityForResult(makeCollection, COLLECTION);
            }
        }

        ArrayList<Collection> collections = new ArrayList<>();

        public void addCollection(Collection collection) {
            collections.add(collection);
            notifyItemInserted(collections.size() - 1);
        }

        @Override
        public int getItemCount() {
            return (isDeviceUser)? (collections.size() + 1) : collections.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (!isDeviceUser || position + 1 != getItemCount())
                ((CollectionHolder)holder).setView(collections.get(position));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate((viewType == 1) ? R.layout.card_add_collection : R.layout.card_collection, parent, false);

            return (viewType == 1) ? new AddCollectionHolder(view) : new CollectionHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return (isDeviceUser && position + 1 == getItemCount())? 1 : 0;
        }
    }

    private class UserGetterTask extends HTTPClient<UserData> {
        @Override
        protected void onPostExecute(JSONObject result) {
            user = new User(result);
            collectionAdapter = new CollectionAdapter();
            GetCollections getCollections = new GetCollections();
            getCollections.start(CollectionData.getCollections(user.getEmail()));
            setView();
            collectionList.setAdapter(collectionAdapter);

            boolean isFollowing = user.isFollowing();
            followOrEdit.setSelected(isFollowing);
            followOrEdit.setText((isFollowing) ? "팔로잉" : "+ 팔로우");
            followOrEdit.setTextColor(getResources().getColor((isFollowing) ? R.color.icons : R.color.accent));
        }
    }

    private class GetCollections extends HTTPClient<CollectionData> {

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                GetThumbnail getThumbnail = new GetThumbnail();
                getThumbnail.execute(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dialog.dismiss();
            }
        }
    }

    private class GetThumbnail extends AsyncTask<JSONObject, Collection, Void> {

        int position;

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected Void doInBackground(JSONObject... params) {
            JSONObject response = params[0];

            try {
                JSONArray collections = response.getJSONArray("collections");

                int arraySize = collections.length();
                for (position = 0; position < arraySize; position++) {
                    Collection collection = new Collection(collections.getJSONObject(position));
                    String path = collection.getThumbnailPath();
                    Bitmap thumbnail = null;
                    if (path != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(path).getContent());
                        thumbnail = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getWidth());
                    }
                    collection.setThumbnail(thumbnail);
                    publishProgress(collection);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Exception
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Collection... values) {
            collectionAdapter.addCollection(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
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
            } finally {
                dialog.dismiss();
            }
        }
    }

    private class UpdateNickName extends HTTPClient<UserData> {

        String nickName;

        public UpdateNickName(String nickName) {
            super();

            this.nickName = nickName;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result.getBoolean("success")) {
                    user.updateNickName(nickName);
                    setView();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dialog.dismiss();
            }
        }
    }

    private class MakeCollection extends HTTPClient<CollectionData> {

        String name;

        public MakeCollection(String name) {
            this.name = name;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                Collection collection = new Collection(result.getInt("id"), user.getEmail(), name, null);

                collectionAdapter.addCollection(collection);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dialog.dismiss();
            }
        }
    }
}
