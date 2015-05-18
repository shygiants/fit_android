package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;

/**
 * Created by SHY_mini on 15. 5. 16..
 */
public class SearchFragment extends Fragment {

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

    private class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public class FilterViewHolder extends RecyclerView.ViewHolder {

            TextView filterText;

            public FilterViewHolder(View view) {
                super(view);

                filterText = (TextView)view.findViewById(R.id.filterText);
            }

            public void setView(String filter) {
                filterText.setText(filter);
            }
        }

        public class FilterAddButtonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public FilterAddButtonHolder(View view) {
                super(view);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                ClassPickerFragment classPickerFragment = new ClassPickerFragment();

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.tabContainer, classPickerFragment)
                        .addToBackStack(null)
                        .commit();

//                getChildFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.container, null) // TODO: replace null with filter fragment
//                        .addToBackStack(null)
//                        .commit();
            }
        }

        ArrayList<String> filters;

        public FilterAdapter() {
            filters = new ArrayList<>();

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position + 1 != getItemCount())
                ((FilterViewHolder)holder).setView(String.valueOf(position));
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
