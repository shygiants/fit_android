package kr.ac.korea.ee.fit.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import kr.ac.korea.ee.fit.request.Event;
import kr.ac.korea.ee.fit.request.Feed;

/**
 * Created by SHYBook_Air on 15. 5. 11..
 */
public class FeedFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String CONTEXT = "CONTEXT";

    public static final String TAB = "TAB";
    public static final String SEARCH = "SEARCH";
    public static final String COLLECTION = "COLLECTION";
    public static final String DETAIL = "DETAIL";

    FashionCardAdapter fashionCardAdapter;
    StaggeredGridLayoutManager fashionFeedLayoutManager;

    public FragmentManager fragmentManager;
    String context;
    int collection_id;
    String user_id;
    boolean moreExists = true;

    ProgressDialog dialog;
    SwipeRefreshLayout swipe;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Bundle arguments = getArguments();
        context = arguments.getString(CONTEXT);
        collection_id = arguments.getInt(CollectionFragment.COLLECTION_ID);
        user_id = arguments.getString(CollectionFragment.USER_ID);

        fashionCardAdapter = new FashionCardAdapter();

        switch (context) {
            case TAB:
                fragmentManager = getFragmentManager();
                break;
            case SEARCH:
            case COLLECTION:
            case DETAIL:
                fragmentManager = getParentFragment().getFragmentManager();
                break;
        }

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("진행중...");
        dialog.setTitle("네트워크 체크");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        swipe = (SwipeRefreshLayout)view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);

        RecyclerView cardList = (RecyclerView)view.findViewById(R.id.cardList);

        Configuration config = getResources().getConfiguration();
        boolean isLarge = (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE;
        fashionFeedLayoutManager =
                new StaggeredGridLayoutManager((isLarge) ? 3 : 2, StaggeredGridLayoutManager.VERTICAL);

        cardList.setLayoutManager(fashionFeedLayoutManager);
        cardList.setAdapter(fashionCardAdapter);
        cardList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = fashionFeedLayoutManager.getChildCount();
                int totalItemCount = fashionFeedLayoutManager.getItemCount();
                int[] pastVisibleItems = new int[fashionFeedLayoutManager.getSpanCount()];
                fashionFeedLayoutManager.findFirstVisibleItemPositions(pastVisibleItems);

                if (moreExists) {
                    int totalPastVisibleItems = 0;
                    for (int count : pastVisibleItems) {
                        totalPastVisibleItems += count;
                    }
                    if (visibleItemCount + totalPastVisibleItems >= totalItemCount) {
                        moreExists = false;
                        fashionCardAdapter.loadMore();
                    }
                }
            }
        });

        return view;
    }

    void startDetailView(FashionCard fashionCard) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle arg = new Bundle();
        arg.putInt(DetailFragment.FASHION_ID, fashionCard.getFashionId());
        arg.putParcelable(DetailFragment.IMAGE, fashionCard.getImage());
        detailFragment.setArguments(arg);

        fragmentManager.beginTransaction()
                .replace(R.id.tabContainer, detailFragment, DETAIL)
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onRefresh() {
        fashionCardAdapter.refresh();
    }

    public void refresh(Feed getFiltered) {
        fashionCardAdapter.refresh(getFiltered);
    }

    private class FashionCardAdapter extends RecyclerView.Adapter<FashionCardAdapter.CardViewHolder> {

        public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView fashionImg;
            TextView editorName;
            TextView vendor;
            ImageButton[] button;
            View cardView;
            int fashionId;

            public CardViewHolder(View view) {
                super(view);
                cardView = view;
                fashionImg = (ImageView)view.findViewById(R.id.fashionImg);
                editorName = (TextView)view.findViewById(R.id.editorName);
                vendor = (TextView)view.findViewById(R.id.vendor);
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
                    button[i].setSelected(i + 1 == ratingType);
                    button[i].setOnClickListener(this);
                }
                String nickname = fashionCard.getNickname();
                editorName.setText((nickname.equals("null"))? fashionCard.getEditorName() : nickname);
                vendor.setText(fashionCard.getVendorName());
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

                dialog.show();
                RateTask rate = new RateTask((ImageButton)view);
                rate.start(Event.rate(User.getDeviceUserId(), fashionId, ratingType));
            }

            class RateTask extends HTTPClient<Event> {

                ImageButton buttonClicked;

                public RateTask(ImageButton buttonClicked) {
                    this.buttonClicked = buttonClicked;
                }
                @Override
                protected void onPostExecute(JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {
                            for (int i = 0; i < 3; i++)
                                button[i].setSelected(false);
                            buttonClicked.setSelected(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // TODO: Exception
                    } finally {
                        dialog.dismiss();
                    }
                }
            }
        }

        ArrayList<FashionCard> cards;
        String[] ratingTypes;
        Feed feed;
        int page = 0;

        public FashionCardAdapter() {
            cards = new ArrayList<>();

            switch (context) {
                case COLLECTION:
                    feed = Feed.getCollection(collection_id, user_id);
                    break;
                case SEARCH:
                    feed = Feed.getFeed();
                    break;
                default:
                    feed = Feed.getRecommended();
            }
            feed.setPage(0);

            FeedTask feeder = new FeedTask();
            feeder.start(feed);
        }

        public void clearCards() {
            for (FashionCard card : cards)
                card.getImage().recycle();
            cards.clear();
        }

        public void refresh(Feed getFiltered) {
            clearCards();
            notifyDataSetChanged();

            feed = getFiltered;
            page = 0;
            feed.setPage(page);
            FeedTask feeder = new FeedTask();
            feeder.start(getFiltered);
        }

        public void refresh() {
            clearCards();
            notifyDataSetChanged();

            page = 0;
            feed.setPage(page);
            FeedTask feeder = new FeedTask();
            feeder.start(feed);
        }

        public void loadMore() {
            page++;
            feed.setPage(page);
            new FeedTask().start(feed);
        }

        public void addCard(FashionCard card) {
            cards.add(card);
            notifyItemInserted(cards.size() - 1);
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

            int get;

            @Override
            protected void onPreExecute() { super.onPreExecute(); }

            @Override
            protected Void doInBackground(JSONObject... params) {
                JSONObject response = params[0];

                try {
                    JSONArray cards = response.getJSONArray("cards");

                    int arraySize = cards.length();
                    get = arraySize;
                    for (int position = 0; position < arraySize; position++) {
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
                addCard(values[0]);
            }

            @Override
            protected void onPostExecute(Void result) {
                swipe.setRefreshing(false);
                if (get == Feed.LIMIT)
                    moreExists = true;
            }
        }
    }
}
