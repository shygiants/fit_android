package kr.ac.korea.ee.fit.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.core.MyLinearLayoutManager;
import kr.ac.korea.ee.fit.model.Fashion;
import kr.ac.korea.ee.fit.model.Comment;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.Event;
import kr.ac.korea.ee.fit.request.Feed;

/**
 * Created by SHYBook_Air on 15. 5. 11..
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    public static final String FASHION_ID = "FashionID";
    public static final String IMAGE = "IMAGE";

    static final float RATED = (float)1.0;
    static final float NOT_RATED = (float)0.26;

    // attributes
    int fashionId;
    Bitmap image;
    Fashion fashion;

    // views
    ImageButton[] rateButtons;
    TextView editorName;
    TextView srcLink;
    RecyclerView commentList;
    EditText writeComment;
    Button submitComment;
    FeedFragment relatedFragment;
    Button viewAllComments;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ((ImageView)view.findViewById(R.id.fashionImg)).setImageBitmap(image);

        rateButtons = new ImageButton[3];
        rateButtons[0] = (ImageButton)view.findViewById(R.id.button01);
        rateButtons[1] = (ImageButton)view.findViewById(R.id.button02);
        rateButtons[2] = (ImageButton)view.findViewById(R.id.button03);
        for (int i = 0; i < 3; i++)
            rateButtons[i].setOnClickListener(this);

        editorName = (TextView)view.findViewById(R.id.editorName);
        srcLink = (TextView)view.findViewById(R.id.srcLink);
        if (fashion != null) {
            editorName.setText(fashion.getEditorName());
            srcLink.setText(fashion.getSrcLink());
            for (int i = 0; i < 3; i++)
                rateButtons[i].setAlpha((i + 1 == fashion.getRate())? RATED : NOT_RATED);
        }

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
    public void onClick(View view) {
        int ratingType;
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
                    writeComment.setText("");
                    Comment input = new Comment(fashionId, comment);
                    CommentTask commentTask = new CommentTask(input);
                    commentTask.start(Event.comment(input));
                }
                clearFocus();
                return;
            case R.id.viewAllComments:
                CommentFragment commentFragment = new CommentFragment();
                Bundle arg = new Bundle();
                arg.putInt(FASHION_ID, fashionId);
                commentFragment.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .replace(R.id.tabContainer, commentFragment)
                        .addToBackStack(null)
                        .commit();
                return;
            default:
                ratingType = 0;
                break;
        }

        RateTask rate = new RateTask((ImageButton)view);
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

                editorName.setText(fashion.getEditorName());
                srcLink.setText("출처 - " + fashion.getSrcLink());

                for (int i = 0; i < 3; i++)
                    rateButtons[i].setAlpha((i + 1 == fashion.getRate())? RATED : NOT_RATED);

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

        public class CommentViewHolder extends RecyclerView.ViewHolder {

            ImageView profile;
            TextView nicknameText;
            TextView commentText;
            TextView likeComment;
            TextView timeText;

            public CommentViewHolder(View view) {
                super(view);

                profile = (ImageView)view.findViewById(R.id.profile);
                nicknameText = (TextView)view.findViewById(R.id.nickname);
                commentText = (TextView)view.findViewById(R.id.comment);
                likeComment = (TextView)view.findViewById(R.id.likeComment);
                timeText = (TextView)view.findViewById(R.id.commentTime);
            }

            public void setView(Comment comment) {
                commentText.setText(comment.getComment());
                nicknameText.setText(comment.getNickname());
                timeText.setText(comment.getCreated());
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

        ImageButton buttonClicked;

        public RateTask(ImageButton buttonClicked) {
            this.buttonClicked = buttonClicked;
        }
        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                String success = response.getString("success");
                if (success.equals("true")) {
                    for (int i = 0; i < 3; i++)
                        rateButtons[i].setAlpha(NOT_RATED);
                    buttonClicked.setAlpha(RATED);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Exception
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
                if (result.getBoolean("success"))
                    commentListAdapter.addComment(comment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
