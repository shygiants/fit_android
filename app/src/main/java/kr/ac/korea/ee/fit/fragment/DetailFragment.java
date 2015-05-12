package kr.ac.korea.ee.fit.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.model.Fashion;
import kr.ac.korea.ee.fit.model.FashionCard;
import kr.ac.korea.ee.fit.request.Detail;

/**
 * Created by SHYBook_Air on 15. 5. 11..
 */
public class DetailFragment extends Fragment {

    public static final String FASHION_ID = "FashionID";
    public static final String IMAGE = "IMAGE";
    int fashionId;
    Bitmap image;

    View detailView;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        fashionId = getArguments().getInt(FASHION_ID);
        image = getArguments().getParcelable(IMAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ((TextView)view.findViewById(R.id.fashionIdText)).setText(String.valueOf(fashionId));

        ((ImageView)view.findViewById(R.id.fashionImg)).setImageBitmap(image);
        detailView = view;

        GetDetailTask getDetail = new GetDetailTask();
        getDetail.start(new Detail(String.valueOf(fashionId)));

        return view;
    }

    void startDetailView(int editorId) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle arg = new Bundle();
        arg.putInt(DetailFragment.FASHION_ID, editorId);
        detailFragment.setArguments(arg);

        getFragmentManager()
            .beginTransaction()
            .replace(R.id.tab, detailFragment)
            .addToBackStack(null)
            .commit();
    }

    private class GetDetailTask extends HTTPClient<Detail> {
        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                JSONArray items = response.getJSONArray("items");
                Fashion fashion = new Fashion(response.getJSONObject("fashion"));
                fashion.setView(detailView);

            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Exception
            }
        }
    }
}
