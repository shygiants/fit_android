package kr.ac.korea.ee.fit.fragment;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.client.ImgGetter;
import kr.ac.korea.ee.fit.model.FashionCard;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.Event;
import kr.ac.korea.ee.fit.request.Feed;

/**
 * Created by SHYBook_Air on 15. 5. 11..
 */
public class FeedFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        RecyclerView cardList = (RecyclerView) view.findViewById(R.id.cardList);

        Configuration config = getResources().getConfiguration();
        boolean isLarge = (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE;
        StaggeredGridLayoutManager fashionFeedLayoutManager =
                new StaggeredGridLayoutManager((isLarge) ? 3 : 2, StaggeredGridLayoutManager.VERTICAL);

        cardList.setLayoutManager(fashionFeedLayoutManager);
        FashionCardAdapter fashionCardAdapter = new FashionCardAdapter(this);
        cardList.setAdapter(fashionCardAdapter);

        return view;
    }

    void startDetailView(int editorId) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle arg = new Bundle();
        arg.putInt(DetailFragment.FASHION_ID, editorId);
        detailFragment.setArguments(arg);

        getParentFragment().getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.tab, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private static class FashionCardAdapter extends RecyclerView.Adapter<FashionCardAdapter.CardViewHolder> {

        public static class CardViewHolder extends RecyclerView.ViewHolder {
            protected ImageView fashionImg;
            protected TextView editorName;
            protected Button[] button;
            protected View cardView;

            FeedFragment feedFragment;

            public CardViewHolder(View view, FeedFragment feedFragment) {
                super(view);
                cardView = view;
                this.feedFragment = feedFragment;
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

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        feedFragment.startDetailView(fashionCard.getFashionId());
                    }
                });

                int ratingType = fashionCard.getRatingType();

                for (int i = 0; i < 3; i++) {
                    button[i].setText(ratingTypes[i]);
                    button[i].setTextColor((i + 1 == ratingType)? 0xFFEF5350 : 0xFF42A5F5);

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
                            try {
                                JSONObject response = rate.get();
                                String success = response.getString("success");
                                if (success.equals("true")) {
                                    for (int i = 0; i < 3; i++)
                                        button[i].setTextColor(0xFF42A5F5);
                                    ((Button)v).setTextColor(0xFFEF5350);
                                }
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

        FeedFragment feedFragment;

        public FashionCardAdapter(FeedFragment feedFragment) {
            this.feedFragment = feedFragment;
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

            return new CardViewHolder(itemView, feedFragment);
        }
    }
}
