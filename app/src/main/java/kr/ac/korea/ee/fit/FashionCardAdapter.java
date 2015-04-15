package kr.ac.korea.ee.fit;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.client.ImgGetter;
import kr.ac.korea.ee.fit.model.FashionCard;
import kr.ac.korea.ee.fit.model.Feed;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class FashionCardAdapter extends BaseAdapter {

    Context context;
    ArrayList<FashionCard> cards;

    public FashionCardAdapter(Context context) {
        this.context = context;
        cards = new ArrayList<>();

        HTTPClient<Feed> feeder = new HTTPClient<>();
        feeder.start(Feed.getFeed());

        JSONObject response = null;
        JSONArray cards_json = null;

        try {
            response = feeder.get();
            cards_json = response.getJSONArray("cards");

            for (int i = 0; i < cards_json.length(); i++)
                cards.add(new FashionCard(cards_json.getJSONObject(i)));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FashionCardAdapter", "Constructor Exception");
        }
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public long getItemId(int position) {
        return cards.get(position).getFashionId();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FashionCard card = cards.get(position);
        if (convertView==null)
            convertView = View.inflate(context, R.layout.card_fashion, null);

        ImgGetter imgGetter = new ImgGetter();
        imgGetter.execute(card.getImgPath());
        try {
            ((ImageView) convertView.findViewById(R.id.fashionImg)).setImageBitmap(imgGetter.get());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FashionCardAdapter", "getView Exception");
        }

        ((TextView)convertView.findViewById(R.id.editorName)).setText(card.getEditorName());

        return convertView;
    }
}
