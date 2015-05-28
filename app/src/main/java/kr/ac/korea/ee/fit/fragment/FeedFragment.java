package kr.ac.korea.ee.fit.fragment;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.model.FashionCard;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.Detail;
import kr.ac.korea.ee.fit.request.Event;
import kr.ac.korea.ee.fit.request.Feed;

/**
 * Created by SHYBook_Air on 15. 5. 11..
 */
public class FeedFragment extends android.support.v4.app.Fragment {

    public static final String CONTEXT = "CONTEXT";

    public static final String TAB = "TAB";
    public static final String SEARCH = "SEARCH";

    FashionCardAdapter fashionCardAdapter;

    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        fashionCardAdapter = new FashionCardAdapter();

        Bundle arguments = getArguments();
        String context = arguments.getString(CONTEXT);

        switch (context) {
            case TAB:
                fragmentManager = getFragmentManager();
                break;
            case SEARCH:
                fragmentManager = getParentFragment().getFragmentManager();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        RecyclerView cardList = (RecyclerView)view.findViewById(R.id.cardList);

        Configuration config = getResources().getConfiguration();
        boolean isLarge = (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE;
        StaggeredGridLayoutManager fashionFeedLayoutManager =
                new StaggeredGridLayoutManager((isLarge) ? 3 : 2, StaggeredGridLayoutManager.VERTICAL);

        cardList.setLayoutManager(fashionFeedLayoutManager);
        cardList.setAdapter(fashionCardAdapter);

        return view;
    }

    void startDetailView(FashionCard fashionCard) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle arg = new Bundle();
        arg.putInt(DetailFragment.FASHION_ID, fashionCard.getFashionId());
        arg.putParcelable(DetailFragment.IMAGE, fashionCard.getImage());
        detailFragment.setArguments(arg);

        fragmentManager.beginTransaction()
                    .replace(R.id.tabContainer, detailFragment)
                    .addToBackStack(null)
                    .commit();
    }

    public void refresh(Feed getFiltered) {
        fashionCardAdapter.refresh(getFiltered);
    }

    private class FashionCardAdapter extends RecyclerView.Adapter<FashionCardAdapter.CardViewHolder> {

        public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            static final float RATED = (float)1.0;
            static final float NOT_RATED = (float)0.26;

            ImageView fashionImg;
            TextView editorName;
            ImageButton[] button;
            View cardView;
            int fashionId;

            public CardViewHolder(View view) {
                super(view);
                cardView = view;
                fashionImg = (ImageView)view.findViewById(R.id.fashionImg);
                editorName = (TextView)view.findViewById(R.id.editorName);
                button = new ImageButton[3];
                button[0] = (ImageButton)view.findViewById(R.id.button1);
                button[1] = (ImageButton)view.findViewById(R.id.button2);
                button[2] = (ImageButton)view.findViewById(R.id.button3);
            }

            public void setView(final FashionCard fashionCard, String[] ratingTypes) {

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDetailView(fashionCard);
                    }
                });

                fashionImg.setImageBitmap(fashionCard.getImage());
                fashionId = fashionCard.getFashionId();

                int ratingType = fashionCard.getRatingType();

                for (int i = 0; i < 3; i++) {
//                    button[i].setText(ratingTypes[i]);
                    button[i].setAlpha((i + 1 == ratingType)? RATED : NOT_RATED);
                    button[i].setOnClickListener(this);
                }
                editorName.setText(fashionCard.getEditorName() + "님이 작성");
            }

            @Override
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
                RateTask rate = new RateTask((ImageButton)view);
                rate.start(ratingEvent);
            }

            class RateTask extends HTTPClient<Event> {

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

        ArrayList<FashionCard> cards;
        String[] ratingTypes;

        public FashionCardAdapter() {
            cards = new ArrayList<>();

            FeedTask feeder = new FeedTask();
            feeder.start(Feed.getFeed());
        }

        public void refresh(Feed getFiltered) {
            cards.clear();
            notifyDataSetChanged();

            FeedTask feeder = new FeedTask();
            feeder.start(getFiltered);
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

        private class FeedTask extends HTTPClient<Feed> {
            @Override
            protected void onPostExecute(JSONObject response) {
                ParsingTask parsingTask = new ParsingTask();
                parsingTask.execute(response);

                try {
                    JSONArray ratingTypes_json = response.getJSONArray("rating_types");
                    ratingTypes = new String[3];
                    int arraySize = ratingTypes_json.length();
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject ratingType = ratingTypes_json.getJSONObject(i);
                        ratingTypes[i] = ratingType.getString("label");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // TODO: Exception
                }
            }
        }

        private class ParsingTask extends AsyncTask<JSONObject, FashionCard, Void> {

            int position;

            @Override
            protected void onPreExecute() { super.onPreExecute(); }

            @Override
            protected Void doInBackground(JSONObject... params) {
                JSONObject response = params[0];

                try {
                    Log.i("json", response.toString());
                    JSONArray cards = response.getJSONArray("cards");

                    int arraySize = cards.length();
                    for (position = 0; position < arraySize; position++) {
                        FashionCard card = new FashionCard(cards.getJSONObject(position));
                        Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(card.getImgPath()).getContent());
                        card.setImage(bitmap);
                        publishProgress(card);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: Exception
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(FashionCard... values) {
                cards.add(values[0]);
                notifyItemInserted(position);
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }
        }
    }
}
