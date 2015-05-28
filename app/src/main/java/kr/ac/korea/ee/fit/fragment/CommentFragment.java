package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.model.CommentItem;

/**
 * Created by NuriKim on 15. 5. 28..
 */
public class CommentFragment extends Fragment {

    public static final String FASHION_ID = "FASHION_ID";

    int fashion_id;

    CommentListAdapter adapter;
    RecyclerView commentList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        fashion_id = args.getInt(FASHION_ID);

        adapter = new CommentListAdapter(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        commentList = (RecyclerView)view.findViewById(R.id.commentList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        commentList.setLayoutManager(linearLayoutManager);
        commentList.setAdapter(adapter);

        return view;
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

            public void setView(CommentItem commentItem) {
                commentText.setText(commentItem.getComment());
                nicknameText.setText(commentItem.getNickname());
            }
        }

        String fashion_id;
        List<CommentItem> commentItems = new ArrayList<>();

        public CommentListAdapter(String fashion_id) {
            this.fashion_id = fashion_id;

            commentItems.add(new CommentItem());
            commentItems.add(new CommentItem());
            commentItems.add(new CommentItem());
        }

        @Override
        public int getItemCount() {
            return commentItems.size();
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            holder.setView(commentItems.get(position));
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_item, parent, false);

            return new CommentViewHolder(view);
        }
    }
}
