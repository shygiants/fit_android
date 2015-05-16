package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;

/**
 * Created by SHY_mini on 15. 5. 16..
 */
public class SearchFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView filterList = (RecyclerView)view.findViewById(R.id.filterList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        filterList.setLayoutManager(linearLayoutManager);

        filterList.setAdapter(new FilterAdapter());
        
        FeedFragment resultFeed = new FeedFragment();

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.result, resultFeed)
                .commit();

        return view;
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

        public class FilterViewHolder extends RecyclerView.ViewHolder {
            public FilterViewHolder(View view) {
                super(view);
            }
        }

        ArrayList<String> filters;

        public FilterAdapter() {
            filters = new ArrayList<>();

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public void onBindViewHolder(FilterViewHolder holder, int position) {

        }

        @Override
        public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }
    }
}
