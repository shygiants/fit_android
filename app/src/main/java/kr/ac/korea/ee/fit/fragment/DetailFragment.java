package kr.ac.korea.ee.fit.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONObject;

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
public class DetailFragment extends Fragment {

    public static final String FASHION_ID = "FashionID";
    public static final String IMAGE = "IMAGE";

    static final float RATED = (float)1.0;
    static final float NOT_RATED = (float)0.26;

    int fashionId;
    Bitmap image;

    View detailView;
    ImageButton[] button;

    CommentFragment commentFragment;

    boolean firstTime = true;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        fashionId = getArguments().getInt(FASHION_ID);
        image = getArguments().getParcelable(IMAGE);

        commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putInt(CommentFragment.FASHION_ID, fashionId);
        commentFragment.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ((ImageView) view.findViewById(R.id.fashionImg)).setImageBitmap(image);

        detailView = view;

        button = new ImageButton[3];
        button[0] = (ImageButton)view.findViewById(R.id.button1);
        button[1] = (ImageButton)view.findViewById(R.id.button2);
        button[2] = (ImageButton)view.findViewById(R.id.button3);

        GetDetailTask getDetail = new GetDetailTask();
        getDetail.start(new Detail(String.valueOf(fashionId)));

        if (firstTime) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.detailContainer, commentFragment)
                    .commit();
            firstTime = false;
        }

        return view;
    }

    public void onClick(View view) {
        int ratingType;
        switch (view.getId()) {
            case R.id.button1:
                ratingType = 1;
                break;
            case R.id.button2:
                ratingType = 2;
                break;
            case R.id.button3:
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

    void startDetailView(int editorId) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle arg = new Bundle();
        arg.putInt(DetailFragment.FASHION_ID, editorId);
        detailFragment.setArguments(arg);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.tabcontent, detailFragment)
                .addToBackStack(null)
                .commit();
    }

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
