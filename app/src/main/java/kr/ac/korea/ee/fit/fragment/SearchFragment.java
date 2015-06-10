package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.model.Filter;
import kr.ac.korea.ee.fit.request.Feed;

/**
 * Created by SHY_mini on 15. 5. 16..
 */
public class SearchFragment extends Fragment {

    public static final String KEY = "SEARCH";

    FeedFragment resultFragment;
    FilterAdapter filterAdapter;

    boolean firstTime = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultFragment = new FeedFragment();
        Bundle arg = new Bundle();
        arg.putString(FeedFragment.CONTEXT, FeedFragment.SEARCH);
        resultFragment.setArguments(arg);

        filterAdapter = new FilterAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView filterList = (RecyclerView)view.findViewById(R.id.filterList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        filterList.setLayoutManager(linearLayoutManager);

        filterList.setAdapter(filterAdapter);

        if (firstTime) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, resultFragment)
                    .commit();
            firstTime = false;
        }

        return view;
    }

    public void addFilter(Filter filter) {
        filterAdapter.addFilter(filter);
        getResult();
    }

    public void modifyFilter(int position, Filter filter) {
        filterAdapter.modifyFilter(position, filter);
        getResult();
    }

    public void removeFilter(int position) {
        filterAdapter.removeFilter(position);
        getResult();
    }

    void getResult() {
        resultFragment.refresh(Feed.getFiltered(filterAdapter.filters));
    }

    public Fragment getFragment() {
        return this;
    }

    private class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public class FilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            Button filterText;

            Filter filter;
            int position;

            public FilterViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);

                filterText = (Button)view.findViewById(R.id.filterText);
            }

            public void setView(int position) {
                this.position = position;
                filter = filters.get(position);
                filterText.setText(filter.getTypeLabel() + "...");
            }

            @Override
            public void onClick(View view) {
                DetailPickerFragment detailPickerFragment = new DetailPickerFragment();
                Bundle args = new Bundle();
                args.putInt(DetailPickerFragment.TYPE_ID, filter.getTypeId());
                args.putString(DetailPickerFragment.TYPE_LABEL, filter.getTypeLabel());
                args.putBoolean(DetailPickerFragment.MODIFYING, true);
                args.putIntegerArrayList(Filter.COLORS, filter.getColors());
                args.putIntegerArrayList(Filter.PATTERNS, filter.getPatterns());
                args.putInt(DetailPickerFragment.POSITION, position);

                getFragmentManager().putFragment(args, KEY, getFragment());
                detailPickerFragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.tabContainer, detailPickerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        public class FilterAddButtonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public FilterAddButtonHolder(View view) {
                super(view);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                ItemPickerFragment itemPickerFragment = new ItemPickerFragment();
                Bundle arg = new Bundle();
                getFragmentManager().putFragment(arg, KEY, getFragment());
                itemPickerFragment.setArguments(arg);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.tabContainer, itemPickerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        ArrayList<Filter> filters;

        public FilterAdapter() {
            filters = new ArrayList<>();
        }

        public void addFilter(Filter filter) {
            filters.add(filter);
            notifyDataSetChanged();
        }

        public void modifyFilter(int position, Filter filter) {
            filters.remove(position);
            filters.add(position, filter);
            notifyDataSetChanged();
        }

        public void removeFilter(int position) {
            filters.remove(position);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return filters.size() + 1;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position + 1 != getItemCount())
                ((FilterViewHolder)holder).setView(position);
        }

        @Override
        public int getItemViewType(int position) {
            return (position + 1 == getItemCount())? 1 : 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate((viewType == 1)? R.layout.button_addfilter : R.layout.card_filter, parent, false);

            return (viewType == 1)? new FilterAddButtonHolder(view) : new FilterViewHolder(view);
        }
    }
}
