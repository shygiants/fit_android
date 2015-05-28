package kr.ac.korea.ee.fit.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
public class DetailFragment extends Fragment{

    public static final String FASHION_ID = "FashionID";
    public static final String IMAGE = "IMAGE";

    static final float RATED = (float)1.0;
    static final float NOT_RATED = (float)0.26;

    int fashionId;
    String key;


    Bitmap image;
    View upCommentView;
    View detailView;
    ImageButton[] button;

    boolean firstTime = true;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        fashionId = getArguments().getInt(FASHION_ID);
        image = getArguments().getParcelable(IMAGE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ((ImageView) view.findViewById(R.id.fashionImg)).setImageBitmap(image);

        detailView = view;

        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putInt(CommentFragment.FASHION_ID, fashionId);
        commentFragment.setArguments(args);

        getFragmentManager().beginTransaction().add(R.id.detailStart, commentFragment).commit();

        SearchFragment relatedFragment = new SearchFragment();
        Bundle argsForRelated = new Bundle();
        argsForRelated.putString(SearchFragment.KEY, key);

        getFragmentManager().beginTransaction().add(R.id.detailStart, relatedFragment).commit();



        TextView moreComment = (TextView)view.findViewById(R.id.moreComment);
        moreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentTabFragment commentTabFragment = new CommentTabFragment();
                Bundle arg = new Bundle();
                commentTabFragment.setArguments(arg);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.tabContainer, commentTabFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });



        button = new ImageButton[3];
        button[0] = (ImageButton)view.findViewById(R.id.button01);
        button[1] = (ImageButton)view.findViewById(R.id.button02);
        button[2] = (ImageButton)view.findViewById(R.id.button03);

    //    GetDetailTask getDetail = new GetDetailTask();
    //    getDetail.start(new Detail(String.valueOf(fashionId)));


        return view;
    }

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
//        RateTask rate = new RateTask((ImageButton)view);
//        rate.start(ratingEvent);
    }
//
//    void startDetailView(int editorId) {
//        DetailFragment detailFragment = new DetailFragment();
//        Bundle arg = new Bundle();
//        arg.putInt(DetailFragment.FASHION_ID, editorId);
//        detailFragment.setArguments(arg);
//
//        getFragmentManager()
//                .beginTransaction()
//                .replace(android.R.id.tabcontent, detailFragment)
//                .addToBackStack(null)
//                .commit();
//    }

    private class GetDetailTask extends HTTPClient<Detail> {
        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                JSONArray items = response.getJSONArray("items");
                Fashion fashion = new Fashion(response.getJSONObject("fashion"));

            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Exception
            }
        }
    }

//    private class RateTask extends HTTPClient<Event> {
//
//        ImageButton buttonClicked;
//
//        public RateTask(ImageButton buttonClicked) {
//            this.buttonClicked = buttonClicked;
//        }
//        @Override
//        protected void onPostExecute(JSONObject response) {
//            try {
//                String success = response.getString("success");
//                if (success.equals("true")) {
//                    for (int i = 0; i < 3; i++)
//                        button[i].setAlpha(NOT_RATED);
//                    buttonClicked.setAlpha(RATED);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                // TODO: Exception
//            }
//        }
//    }


}
