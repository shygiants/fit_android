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
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
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
import kr.ac.korea.ee.fit.model.Collection;
import kr.ac.korea.ee.fit.model.FashionCard;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.CollectionData;
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

    private class FashionCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView fashionImg;
            TextView editorName;
            TextView vendor;
            ImageButton[] button;
            View cardView;
            int fashionId;
            FashionCard fashionCard;
            TextView descText;

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
                if (context == TAB) {
                    descText = (TextView) view.findViewById(R.id.desc);
                    descText.setVisibility(View.VISIBLE);
                    descText.setText("회원님의 취향에 맞는 패션");
                }
            }

            public void setView(FashionCard fashionCard, String[] ratingTypes) {

                this.fashionCard = fashionCard;
                cardView.setOnClickListener(this);
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
                        startDetailView(fashionCard);
                        return;
                }

                dialog.show();
                RateTask rate = new RateTask(ratingType);
                rate.start(Event.rate(User.getDeviceUserId(), fashionId, ratingType));
            }

            class RateTask extends HTTPClient<Event> {

                int ratingType;

                public RateTask(int ratingType) {
                    this.ratingType = ratingType;
                }
                @Override
                protected void onPostExecute(JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {
                            for (int i = 0; i < 3; i++)
                                button[i].setSelected(false);
                            button[ratingType - 1].setSelected(true);
                            fashionCard.setRatingType(ratingType);
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

        public class CollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView thumbnail;
            TextView collectionTitle;
            Collection collection;
            TextView makerNameText;
            TextView descText;

            public CollectionHolder(View view) {
                super(view);

                view.findViewById(R.id.maker).setVisibility(View.VISIBLE);

                thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
                collectionTitle = (TextView)view.findViewById(R.id.collectionTitle);
                makerNameText = (TextView)view.findViewById(R.id.makerName);
                descText = (TextView)view.findViewById(R.id.desc);
                descText.setVisibility(View.VISIBLE);
                descText.setText("인기있는 컬렉션");

                view.setOnClickListener(this);
            }

            public void setView(Collection collection) {
                this.collection = collection;
                collectionTitle.setText(collection.getCollectionName());
                String nickname = collection.getMakerNickname();
                makerNameText.setText((nickname.equals("null")) ? collection.getMakerName() : nickname);
                Bitmap tn = collection.getThumbnail();
                if (tn != null)
                    thumbnail.setImageBitmap(tn);
                else
                    thumbnail.setImageDrawable(getResources().getDrawable(R.mipmap.bg_collection_not_inserted));
            }

            @Override
            public void onClick(View v) {
                CollectionFragment collectionFragment = new CollectionFragment();
                Bundle arg = new Bundle();
                arg.putInt(CollectionFragment.COLLECTION_ID, collection.getCollectionId());
                arg.putString(CollectionFragment.USER_ID, User.getDeviceUserId());
                arg.putString(CollectionFragment.NAME, collection.getCollectionName());
                arg.putString(CollectionFragment.DESC, collection.getCollectionDesc());
                collectionFragment.setArguments(arg);

                getFragmentManager().beginTransaction()
                        .replace(R.id.tabContainer, collectionFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        ArrayList<FashionCard> cards = new ArrayList<>();
        ArrayList<Collection> collections = new ArrayList<>();
        ArrayList<Pair<Boolean, Integer>> isCard = new ArrayList<>();
        String[] ratingTypes;
        Feed feed;
        CollectionData getPopular;
        int page = 0;

        public FashionCardAdapter() {
            switch (context) {
                case COLLECTION:
                    feed = Feed.getCollection(collection_id, user_id);
                    break;
                case SEARCH:
                    feed = Feed.getFeed();
                    break;
                default:
                    feed = Feed.getRecommended();
                    getPopular = CollectionData.getPopular();
                    getPopular.setPage(0);
            }
            feed.setPage(0);

            FeedTask feeder = new FeedTask();
            feeder.start(feed);
            if (getPopular != null)
                new GetCollections().start(getPopular);
        }

        public void clearCards() {
            for (FashionCard card : cards)
                card.getImage().recycle();
            cards.clear();
            isCard.clear();
            notifyDataSetChanged();
        }

        public void clear() {
            for (FashionCard card : cards)
                card.getImage().recycle();
            for (Collection collection : collections)
                collection.getThumbnail().recycle();

            cards.clear();
            collections.clear();
            isCard.clear();
            notifyDataSetChanged();
        }

        public void refresh(Feed getFiltered) {
            clearCards();

            feed = getFiltered;
            page = 0;
            feed.setPage(page);
            FeedTask feeder = new FeedTask();
            feeder.start(getFiltered);
        }

        public void refresh() {
            if (context == TAB)
                clear();
            else
                clearCards();

            page = 0;
            feed.setPage(page);
            new FeedTask().start(feed);
            if (getPopular != null) {
                getPopular.setPage(page);
                new GetCollections().start(getPopular);
            }
        }

        public void loadMore() {
            page++;
            feed.setPage(page);
            if (getPopular != null) {
                getPopular.setPage(page);
                new GetCollections().start(getPopular);
            }

            new FeedTask().start(feed);
        }

        public void addCard(FashionCard card) {
            cards.add(card);
            isCard.add(new Pair<>(true, cards.size() - 1));
            notifyItemInserted(getItemCount() - 1);
        }

        public void addCollection(Collection collection) {
            collections.add(collection);
            isCard.add(new Pair<>(false, collections.size() - 1));
            notifyItemInserted(getItemCount() - 1);
        }

        @Override
        public int getItemCount() {
            return isCard.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (isCard.get(position).first)? 1 : 0;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder.getClass().equals(CardViewHolder.class))
                ((CardViewHolder)viewHolder).setView(cards.get(isCard.get(position).second), ratingTypes);
            else
                ((CollectionHolder)viewHolder).setView(collections.get(isCard.get(position).second));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate((viewType == 1)? R.layout.card_fashion : R.layout.card_collection, viewGroup, false);

            return (viewType == 1)? new CardViewHolder(itemView) : new CollectionHolder(itemView);
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

        private class GetCollections extends HTTPClient<CollectionData> {

            @Override
            protected void onPostExecute(JSONObject result) {
                try {
                    GetThumbnail getThumbnail = new GetThumbnail();
                    getThumbnail.execute(result);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dialog.dismiss();
                    swipe.setRefreshing(false);
                }
            }
        }

        private class GetThumbnail extends AsyncTask<JSONObject, Collection, Void> {

            int position;

            @Override
            protected void onPreExecute() { super.onPreExecute(); }

            @Override
            protected Void doInBackground(JSONObject... params) {
                JSONObject response = params[0];

                try {
                    JSONArray collections = response.getJSONArray("collections");

                    int arraySize = collections.length();
                    for (position = 0; position < arraySize; position++) {
                        Collection collection = new Collection(collections.getJSONObject(position), true);
                        String path = collection.getThumbnailPath();
                        Bitmap thumbnail = null;
                        if (path != null) {
                            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(path).getContent());
                            thumbnail = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getWidth());
                            bitmap.recycle();
                        }
                        collection.setThumbnail(thumbnail);
                        publishProgress(collection);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: Exception
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Collection... values) {
                addCollection(values[0]);
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }
        }
    }
}
