package kr.ac.korea.ee.fit.fragment;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.core.MyLinearLayoutManager;
import kr.ac.korea.ee.fit.model.Comment;
import kr.ac.korea.ee.fit.request.Event;
import kr.ac.korea.ee.fit.request.Feed;

/**
 * Created by NuriKim on 15. 5. 28..
 */
public class CommentFragment extends Fragment implements View.OnClickListener {

    int fashion_id;

    CommentListAdapter adapter;
    RecyclerView commentList;
    EditText writeComment;
    Button submitComment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        fashion_id = args.getInt(DetailFragment.FASHION_ID);
        adapter = new CommentListAdapter();
        GetComments getComments = new GetComments();
        getComments.start(Feed.getComments(fashion_id));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        view.findViewById(R.id.backButton).setOnClickListener(this);

        commentList = (RecyclerView)view.findViewById(R.id.commentList);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        commentList.setLayoutManager(linearLayoutManager);
        commentList.setAdapter(adapter);

        writeComment = (EditText)view.findViewById(R.id.writeComment);
        submitComment = (Button)view.findViewById(R.id.submit);
        submitComment.setOnClickListener(this);

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
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().onBackPressed();
                break;
            case R.id.submit:
                String comment = writeComment.getText().toString();
                if (comment.length() > 0) {
                    writeComment.setText("");
                    Comment input = new Comment(fashion_id, comment);
                    PostComment postComment = new PostComment(input);
                    postComment.start(Event.comment(input));
                }
                clearFocus();
                break;
        }
    }

    void clearFocus() {
        writeComment.clearFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(writeComment.getWindowToken(), 0);
    }

    private class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

        public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView profile;
            TextView nicknameText;
            TextView commentText;
            TextView likeComment;
            TextView timeText;

            Comment comment;

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
                nicknameText.setOnClickListener(this);
                timeText.setText(comment.getCreated());

                this.comment = comment;
            }

            @Override
            public void onClick(View v) {
                UserFragment editorFragment = new UserFragment();
                Bundle arg = new Bundle();
                arg.putString(UserFragment.USER_ID, comment.getUserId());
                editorFragment.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .replace(R.id.tabContainer, editorFragment)
                        .addToBackStack(null)
                        .commit();
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

    private class PostComment extends HTTPClient<Event> {

        Comment comment;

        public PostComment(Comment comment) {
            this.comment = comment;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result.getBoolean("success"))
                    adapter.addComment(comment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetComments extends HTTPClient<Feed> {
        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                JSONArray comments = response.getJSONArray("comments");

                int size = comments.length();
                for (int i = 0; i < size; i++)
                    adapter.addComment(new Comment(comments.getJSONObject(i)));

            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Exception
            }
        }
    }
}
