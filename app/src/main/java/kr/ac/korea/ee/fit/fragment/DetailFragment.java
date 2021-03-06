package kr.ac.korea.ee.fit.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import kr.ac.korea.ee.fit.core.MyLinearLayoutManager;
import kr.ac.korea.ee.fit.model.Collection;
import kr.ac.korea.ee.fit.model.Fashion;
import kr.ac.korea.ee.fit.model.Comment;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.CollectionData;
import kr.ac.korea.ee.fit.request.Event;
import kr.ac.korea.ee.fit.request.Feed;
import kr.ac.korea.ee.fit.request.UserData;

/**
 * Created by SHYBook_Air on 15. 5. 11..
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    public static final String FASHION_ID = "FashionID";
    public static final String IMAGE = "IMAGE";
    public static final String NAME = "NAME";
    public static final String DESC = "DESC";
    public static final int COLLECTION = 3;

    // attributes
    int fashionId;
    Bitmap image;
    Fashion fashion;

    // views
    ImageButton[] rateButtons;
    TextView editorName;
    TextView vendorName;
    Button followButton;
    RecyclerView commentList;
    EditText writeComment;
    Button submitComment;
    FeedFragment relatedFragment;
    Button viewAllComments;


    ProgressDialog dialog;
    CommentListAdapter commentListAdapter;
    boolean firstTime = true;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        fashionId = getArguments().getInt(FASHION_ID);
        image = getArguments().getParcelable(IMAGE);

        GetDetailTask getDetail = new GetDetailTask();
        getDetail.start(Feed.getDetail(fashionId));

        commentListAdapter = new CommentListAdapter();

        relatedFragment = new FeedFragment();
        Bundle arg = new Bundle();
        arg.putString(FeedFragment.CONTEXT, FeedFragment.DETAIL);
        relatedFragment.setArguments(arg);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("진행중...");
        dialog.setTitle("네트워크 체크");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView fashionImg = (ImageView) view.findViewById(R.id.fashionImg);
        fashionImg.setImageBitmap(image);
        fashionImg.setOnClickListener(this);

        Button collectButton = (Button)view.findViewById(R.id.collect);
        collectButton.setOnClickListener(this);

        rateButtons = new ImageButton[3];
        rateButtons[0] = (ImageButton)view.findViewById(R.id.button01);
        rateButtons[1] = (ImageButton)view.findViewById(R.id.button02);
        rateButtons[2] = (ImageButton)view.findViewById(R.id.button03);
        for (int i = 0; i < 3; i++)
            rateButtons[i].setOnClickListener(this);

        // editor card
        editorName = (TextView)view.findViewById(R.id.editorName);
        editorName.setOnClickListener(this);
        vendorName = (TextView)view.findViewById(R.id.vendorName);
        followButton = (Button)view.findViewById(R.id.follow);
        followButton.setOnClickListener(this);
        if (fashion != null) {
            String nickname = fashion.getNickname();
            editorName.setText((nickname.equals("null"))? fashion.getEditorName() : nickname);
            vendorName.setText(fashion.getVendorName());

            if (fashion.getEditorId().equals(User.getDeviceUserId()))
                followButton.setVisibility(View.INVISIBLE);
            else {
                boolean isFollowing = fashion.getFollowing();
                followButton.setSelected(isFollowing);
                followButton.setText((isFollowing) ? "팔로잉" : "+ 팔로우");
                followButton.setTextColor(getResources().getColor((isFollowing)? R.color.icons : R.color.accent));
            }
            for (int i = 0; i < 3; i++)
                rateButtons[i].setSelected(i + 1 == fashion.getRate());
        }

        // comment card
        viewAllComments = (Button)view.findViewById(R.id.viewAllComments);
        viewAllComments.setOnClickListener(this);
        commentList = (RecyclerView)view.findViewById(R.id.commentList);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        commentList.setLayoutManager(linearLayoutManager);
        commentList.setAdapter(commentListAdapter);
        writeComment = (EditText)view.findViewById(R.id.writeComment);
        submitComment = (Button)view.findViewById(R.id.submit);
        submitComment.setOnClickListener(this);

        if (firstTime) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, relatedFragment)
                    .commit();
            firstTime = false;
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (writeComment.isFocused()) {
                        Rect outRect = new Rect();
                        writeComment.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                            clearFocus();
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case COLLECTION:
                    dialog.show();
                    String name = data.getStringExtra(NAME);
                    String description = data.getStringExtra(DESC);
                    MakeCollection makeCollection = new MakeCollection();
                    makeCollection.start(CollectionData.makeCollection(name, description));
                    break;
            }
    }

    @Override
    public void onClick(View view) {
        int ratingType;
        AlertDialog.Builder builder;
        Bundle arg = new Bundle();
        switch (view.getId()) {
            case R.id.button01:
                ratingType = 1;
                break;
            case R.id.button02:
                ratingType = 2;
                break;
            case R.id.button03:
                ratingType = 3;
                break;
            case R.id.submit:
                String comment = writeComment.getText().toString();
                if (comment.length() > 0) {
                    dialog.show();
                    Comment input = new Comment(fashionId, comment);
                    CommentTask commentTask = new CommentTask(input);
                    commentTask.start(Event.comment(input));
                }
                clearFocus();
                return;
            case R.id.viewAllComments:
                CommentFragment commentFragment = new CommentFragment();
                arg.putInt(FASHION_ID, fashionId);
                commentFragment.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .replace(R.id.tabContainer, commentFragment)
                        .addToBackStack(null)
                        .commit();
                return;
            case R.id.editorName:
                UserFragment editorFragment = new UserFragment();
                arg.putString(UserFragment.USER_ID, fashion.getEditorId());
                editorFragment.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .replace(R.id.tabContainer, editorFragment)
                        .addToBackStack(null)
                        .commit();
                return;
            case R.id.follow:
                dialog.show();
                FollowTask followTask = new FollowTask();
                followTask.start(Event.follow(fashion.getEditorId()));
                return;
            case R.id.fashionImg:
                builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("사진의 출처로 이동합니다")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fashion.getSrcLink()));
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("취소", null).show();
                return;
            case R.id.collect:
                if (fashion.getRateId() == 0) {
                    builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("컬렉션에 담기 전에 먼저 평가부터 해주세요")
                            .setPositiveButton("확인", null).show();
                    return;
                }
                dialog.show();
                GetCollections getCollections = new GetCollections();
                getCollections.start(CollectionData.getCollections(User.getDeviceUserId()));
                return;
            default:
                ratingType = 0;
                break;
        }
        dialog.show();
        RateTask rate = new RateTask(ratingType);
        rate.start(Event.rate(User.getDeviceUserId(), fashionId, ratingType));
    }

    void clearFocus() {
        writeComment.clearFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(writeComment.getWindowToken(), 0);
    }

    private class GetDetailTask extends HTTPClient<Feed> {
        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                JSONArray items = response.getJSONArray("items");
                JSONArray comments = response.getJSONArray("comments");
                fashion = new Fashion(response.getJSONObject("fashion"));

                String nickname = fashion.getNickname();
                editorName.setText((nickname.equals("null"))? fashion.getEditorName() : nickname);
                vendorName.setText(fashion.getVendorName());

                if (fashion.getEditorId().equals(User.getDeviceUserId()))
                    followButton.setVisibility(View.INVISIBLE);
                else {
                    boolean isFollowing = fashion.getFollowing();
                    followButton.setSelected(isFollowing);
                    followButton.setText((isFollowing) ? "팔로잉" : "+ 팔로우");
                    followButton.setTextColor(getResources().getColor((isFollowing) ? R.color.icons : R.color.accent));
                }

                for (int i = 0; i < 3; i++)
                    rateButtons[i].setSelected(i + 1 == fashion.getRate());

                int size = comments.length();
                for (int i = 0; i < size; i++)
                    commentListAdapter.addComment(new Comment(comments.getJSONObject(i)));

            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Exception
            }
        }
    }

    private class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

        public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView profile;
            TextView nicknameText;
            TextView commentText;
            TextView likeComment;
            TextView numOfLikes;
            TextView timeText;

            Comment comment;

            public CommentViewHolder(View view) {
                super(view);

                profile = (ImageView)view.findViewById(R.id.profile);
                nicknameText = (TextView)view.findViewById(R.id.nickname);
                commentText = (TextView)view.findViewById(R.id.comment);
                likeComment = (TextView)view.findViewById(R.id.likeComment);
                likeComment.setOnClickListener(this);
                numOfLikes = (TextView)view.findViewById(R.id.numOfLikes);
                timeText = (TextView)view.findViewById(R.id.commentTime);
            }

            public void setView(Comment comment) {
                commentText.setText(comment.getComment());
                nicknameText.setText(comment.getNickname());
                nicknameText.setOnClickListener(this);
                likeComment.setText((comment.userLikes()) ? "좋아요 취소" : "좋아요");
                numOfLikes.setText(String.valueOf(comment.getNumOfLikes()));
                timeText.setText(comment.getCreated());

                this.comment = comment;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.nickname:
                        UserFragment editorFragment = new UserFragment();
                        Bundle arg = new Bundle();
                        arg.putString(UserFragment.USER_ID, comment.getUserId());
                        editorFragment.setArguments(arg);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.tabContainer, editorFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.likeComment:
                        dialog.show();
                        LikeComment likeComment = new LikeComment(this);
                        likeComment.start(Event.likeComment(comment.getId()));
                        break;
                }
            }
        }

        ArrayList<Comment> comments = new ArrayList<>();

        @Override
        public int getItemCount() {
            return comments.size();
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            holder.setView(comments.get(position));
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_item, parent, false);

            return new CommentViewHolder(view);
        }

        public void addComment(Comment comment) {
            comments.add(comment);
            notifyDataSetChanged();
        }
    }

    private class RateTask extends HTTPClient<Event> {
        int ratingType;

        public RateTask(int ratingType) {
            this.ratingType = ratingType;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                if (response.getBoolean("success")) {
                    fashion.setRateId(response.getInt("insert_id"));
                    for (int i = 0; i < 3; i++)
                        rateButtons[i].setSelected(false);
                    rateButtons[ratingType - 1].setSelected(true);
                    fashion.setRate(ratingType);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Exception
            } finally {
                dialog.dismiss();
            }
        }
    }

    private class CommentTask extends HTTPClient<Event> {

        Comment comment;

        public CommentTask(Comment comment) {
            this.comment = comment;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                comment.setId(result.getInt("comment_id"));
                writeComment.setText("");
                commentListAdapter.addComment(comment);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dialog.dismiss();
            }
        }
    }

    private class FollowTask extends HTTPClient<Event> {

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                boolean isFollowing = result.getBoolean("is_following");
                followButton.setSelected(isFollowing);
                followButton.setText((isFollowing) ? "팔로잉" : "+ 팔로우");
                followButton.setTextColor(getResources().getColor((isFollowing) ? R.color.icons : R.color.accent));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dialog.dismiss();
            }
        }
    }

    private class LikeComment extends HTTPClient<Event> {

        TextView likeText;
        TextView numOfLikes;

        public LikeComment(CommentListAdapter.CommentViewHolder commentViewHolder) {
            likeText = commentViewHolder.likeComment;
            numOfLikes = commentViewHolder.numOfLikes;
        }
        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                boolean like = result.getBoolean("like");
                likeText.setText((like) ? "좋아요 취소" : "좋아요");
                int num = Integer.parseInt(numOfLikes.getText().toString());
                num += (like)? 1 : (-1);
                numOfLikes.setText(String.valueOf(num));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dialog.dismiss();
            }
        }
    }

    private class GetCollections extends HTTPClient<CollectionData> {

        ArrayList<Collection> collections = new ArrayList<>();

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                int position;
                JSONArray collections_json = result.getJSONArray("collections");

                int arraySize = collections_json.length();
                for (position = 0; position < arraySize; position++) {
                    Collection collection = new Collection(collections_json.getJSONObject(position), false);
//                    String path = collection.getThumbnailPath();
//                    Bitmap thumbnail = null;
//                    if (path != null) {
//                        Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(path).getContent());
//                        thumbnail = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getWidth());
//                    }
//                    collection.setThumbnail(thumbnail);
                    collections.add(collection);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Exception
            }

            BottomSheet.Builder bottomSheetBuilder = new BottomSheet.Builder(getActivity());
            bottomSheetBuilder.title("컬렉션에 담기");
            bottomSheetBuilder.sheet(0, R.drawable.ic_add_box_black_24dp, R.string.add_collection);
            for (Collection collection : collections) {
                if (collection.getCollectionId() != 0)
                    bottomSheetBuilder.sheet(collection.getCollectionId(), collection.getCollectionName());
            }
            bottomSheetBuilder.listener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int which) {
                    if (which != 0) {
                        dialog.show();
                        new Collect().start(Event.collect(fashion.getRateId(), which));
                    }
                    else {
                        Intent makeCollection = new Intent(getActivity(), CollectionActivity.class);
                        getActivity().startActivityForResult(makeCollection, COLLECTION);
                    }
                }
            }).show();

            dialog.dismiss();
        }
    }

    private class Collect extends HTTPClient<Event> {

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result.getBoolean("success")) {
                    Toast.makeText(getActivity(), "컬렉션에 담았습니다", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dialog.dismiss();
            }
        }
    }

    private class MakeCollection extends HTTPClient<CollectionData> {

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                new Collect().start(Event.collect(fashion.getRateId(), result.getInt("id")));
            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
            }
        }
    }
}
