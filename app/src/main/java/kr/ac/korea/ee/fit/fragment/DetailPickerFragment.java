package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.model.SchemaData;
import kr.ac.korea.ee.fit.request.Schema;

/**
 * Created by SHYBook_Air on 15. 5. 19..
 */
public class DetailPickerFragment extends Fragment {

    public static final String TYPE_ID = "TYPE ID";
    public static final String TYPE_LABEL = "TYPE LABEL";

    int id;
    String typeLabel;

    AttributeListAdapter attributeListAdapter;

    ArrayList<Integer> colorFilters;
    ArrayList<Integer> patternFilters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        id = args.getInt(TYPE_ID);
        typeLabel = args.getString(TYPE_LABEL);

        attributeListAdapter = new AttributeListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailpicker, container, false);

        ((TextView)view.findViewById(R.id.typeTitle)).setText(typeLabel);

        RecyclerView attributeList = (RecyclerView)view.findViewById(R.id.attributeList);
        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        attributeList.setLayoutManager(linearLayoutManager);

        attributeList.setAdapter(attributeListAdapter);

        return view;
    }

    void Commit() {

    }

    private class AttributeListAdapter extends RecyclerView.Adapter<AttributeListAdapter.AttributeListHolder> {

        public class AttributeListHolder extends RecyclerView.ViewHolder {

            RecyclerView attributeList;
            TextView attributeLabel;

            public AttributeListHolder(View view) {
                super(view);

                attributeList = (RecyclerView)view.findViewById(R.id.attributeList);
                attributeLabel = (TextView)view.findViewById(R.id.attributeText);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.HORIZONTAL, false);
                attributeList.setLayoutManager(gridLayoutManager);
            }

            public void setView(int viewType) {
                attributeLabel.setText((viewType == 0)? "색깔" : "무늬");
                attributeList.setAdapter(new AttributeAdapter(viewType));
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public void onBindViewHolder(AttributeListHolder holder, int position) {
            holder.setView(position);
        }

        @Override
        public AttributeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.list_attribute, parent, false);

            return new AttributeListHolder(view);
        }
    }

    private class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.AttributeHolder> {

        public class AttributeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            int id;
            TextView attributeLabel;

            public AttributeHolder(View view) {
                super(view);
                attributeLabel = (TextView)view.findViewById(R.id.itemText);
                view.setOnClickListener(this);
            }

            public void setView(Pair<Integer, String> attribute) {
                id = attribute.first;
                attributeLabel.setText(attribute.second);
            }

            @Override
            public void onClick(View view) {
                // TODO: add filter or remove filter
                // TODO: changer view
            }
        }

        ArrayList<Pair<Integer, String>> attributeList;

        public AttributeAdapter(int viewType) {
            attributeList = (viewType == 0)? SchemaData.getColorList() : SchemaData.getPatternList();
        }

        @Override
        public int getItemCount() {
            return attributeList.size();
        }

        @Override
        public void onBindViewHolder(AttributeHolder holder, int position) {
            holder.setView(attributeList.get(position));
        }

        @Override
        public AttributeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.card_item, parent, false);

            return new AttributeHolder(view);
        }
    }
}
