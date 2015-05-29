package kr.ac.korea.ee.fit.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.model.Fashion;
import kr.ac.korea.ee.fit.model.CommentItem;
import kr.ac.korea.ee.fit.model.FashionCard;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.Detail;
import kr.ac.korea.ee.fit.request.Event;

/**
 * Created by SHYBook_Air on 15. 5. 11..
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    public static final String FASHION_ID = "FashionID";
    public static final String IMAGE = "IMAGE";

    static final float RATED = (float)1.0;
    static final float NOT_RATED = (float)0.26;

    int fashionId;
    Bitmap image;

    View detailView;
    RecyclerView commentList;
    CommentListAdapter commentListAdapter;
    ImageButton[] button;
    FeedFragment relatedFragment;
    TextView editorName;
    TextView srcLink;

    boolean firstTime = true;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        fashionId = getArguments().getInt(FASHION_ID);
        image = getArguments().getParcelable(IMAGE);
        commentListAdapter = new CommentListAdapter(fashionId);

        relatedFragment = new FeedFragment();
        Bundle arg = new Bundle();
        arg.putString(FeedFragment.CONTEXT, FeedFragment.DETAIL);
        relatedFragment.setArguments(arg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ((ImageView) view.findViewById(R.id.fashionImg)).setImageBitmap(image);

        editorName = (TextView)view.findViewById(R.id.editorName);
        srcLink = (TextView)view.findViewById(R.id.srcLink);

//        CommentFragment commentFragment = new CommentFragment();
//        Bundle args = new Bundle();
//        args.putInt(CommentFragment.FASHION_ID, fashionId);
//        commentFragment.setArguments(args);

        commentList = (RecyclerView)view.findViewById(R.id.commentList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        commentList.setLayoutManager(linearLayoutManager);
        commentList.setAdapter(commentListAdapter);

        button = new ImageButton[3];
        button[0] = (ImageButton)view.findViewById(R.id.button01);
        button[1] = (ImageButton)view.findViewById(R.id.button02);
        button[2] = (ImageButton)view.findViewById(R.id.button03);
        for (int i = 0; i < 3; i++) {
            button[i].setOnClickListener(this);
        }

        if (firstTime) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, relatedFragment)
                    .commit();
            firstTime = false;
        }

        GetDetailTask getDetail = new GetDetailTask();
        getDetail.start(new Detail(String.valueOf(fashionId)));

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
            default:
                ratingType = 0;
                break;
        }

        Event ratingEvent = new Event(User.get().getEmail(), fashionId, ratingType);
        RateTask rate = new RateTask((ImageButton)view);
        rate.start(ratingEvent);
    }

    private class GetDetailTask extends HTTPClient<Detail> {
        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                JSONArray items = response.getJSONArray("items");
                Fashion fashion = new Fashion(response.getJSONObject("fashion"));

                editorName.setText(fashion.getEditorName());
                srcLink.setText("출처 - " + fashion.getSrcLink());

                for (int i = 0; i < 3; i++)
                    button[i].setAlpha((i + 1 == fashion.getRate())? RATED : NOT_RATED);
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

            public void setView(CommentItem commentItem) {
                commentText.setText(commentItem.getComment());
                nicknameText.setText(commentItem.getNickname());
            }
        }

        int fashion_id;
        List<CommentItem> commentItems = new ArrayList<>();

        public CommentListAdapter(int fashion_id) {
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
                        button[i].setAlpha(NOT_RATED);
                    buttonClicked.setAlpha(RATED);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Exception
            }
        }
    }
}
