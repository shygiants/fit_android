package kr.ac.korea.ee.fit;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.client.ImgGetter;
import kr.ac.korea.ee.fit.model.FashionCard;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.Event;
import kr.ac.korea.ee.fit.request.Feed;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class FashionCardAdapter extends RecyclerView.Adapter<FashionCardAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        protected ImageView fashionImg;
        protected TextView editorName;
        protected Button[] button;

        public CardViewHolder(View view) {
            super(view);
            fashionImg = (ImageView)view.findViewById(R.id.fashionImg);
            editorName = (TextView)view.findViewById(R.id.editorName);
            button = new Button[3];
            button[0] = (Button)view.findViewById(R.id.button1);
            button[1] = (Button)view.findViewById(R.id.button2);
            button[2] = (Button)view.findViewById(R.id.button3);
        }

        public void setView(final FashionCard fashionCard, String[] ratingTypes) {
            ImgGetter imgGetter = new ImgGetter();
            imgGetter.execute(fashionCard.getImgPath());
            try {
                fashionImg.setImageBitmap(imgGetter.get());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("FashionCardAdapter", "getView Exception");
            }
            int i;
            for (i = 0; i < 3; i++) {
                button[i].setText(ratingTypes[i]);
                button[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int ratingType;
                        switch (v.getId()) {
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
                        Event ratingEvent = new Event(User.get().getEmail(), fashionCard.getFashionId(), ratingType);
                        HTTPClient<Event> rate = new HTTPClient<>();
                        rate.start(ratingEvent);
                        JSONObject response = null;
                        try {
                            response = rate.get();
                            String success = response.getString("success");
                            ((Button)v).setText(success);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            editorName.setText(fashionCard.getEditorName() + "님이 작성");
        }
    }

    ArrayList<FashionCard> cards;
    String[] ratingTypes;

    public FashionCardAdapter() {
        cards = new ArrayList<>();

        HTTPClient<Feed> feeder = new HTTPClient<>();
        feeder.start(Feed.getFeed());

        try {
            JSONObject response = feeder.get();
            JSONArray cards_json = response.getJSONArray("cards");
            JSONArray ratingTypes_json = response.getJSONArray("rating_types");

            for (int i = 0; i < cards_json.length(); i++) {
                JSONObject card_json = cards_json.getJSONObject(i);
                cards.add(new FashionCard(card_json));
            }
            ratingTypes = new String[3];
            for (int i = 0; i < ratingTypes_json.length(); i++) {
                JSONObject ratingType_json = ratingTypes_json.getJSONObject(i);
                ratingTypes[i] = ratingType_json.getString("label");
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
        cardViewHolder.setView(card, ratingTypes);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int layout) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_fashion, viewGroup, false);

        return new CardViewHolder(itemView);
    }

}
