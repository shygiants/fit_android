package kr.ac.korea.ee.fit;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.client.ImgGetter;
import kr.ac.korea.ee.fit.model.FashionCard;
import kr.ac.korea.ee.fit.model.Feed;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class FashionCardAdapter extends RecyclerView.Adapter<FashionCardAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        protected ImageView fashionImg;
        protected TextView editorName;

        public CardViewHolder(View view) {
            super(view);
            fashionImg = (ImageView)view.findViewById(R.id.fashionImg);
            editorName = (TextView)view.findViewById(R.id.editorName);
        }

        public void setView(FashionCard fashionCard) {
            ImgGetter imgGetter = new ImgGetter();
            imgGetter.execute(fashionCard.getImgPath());
            try {
                fashionImg.setImageBitmap(imgGetter.get());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("FashionCardAdapter", "getView Exception");
            }

            editorName.setText(fashionCard.getEditorName() + "님이 작성");
        }
    }

    ArrayList<FashionCard> cards;

    public FashionCardAdapter() {
        cards = new ArrayList<>();

        HTTPClient<Feed> feeder = new HTTPClient<>();
        feeder.start(Feed.getFeed());

        try {
            JSONObject response = feeder.get();
            JSONArray cards_json = response.getJSONArray("cards");

            for (int i = 0; i < cards_json.length(); i++) {
                JSONObject card_json = cards_json.getJSONObject(i);
                Log.i("FashionCardAdapter", card_json.getString("img_path"));
                cards.add(new FashionCard(card_json));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FashionCardAdapter", "Constructor Exception");
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int position) {
        FashionCard card = cards.get(position);
        cardViewHolder.setView(card);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int layout) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_fashion, viewGroup, false);

        return new CardViewHolder(itemView);
    }

}
