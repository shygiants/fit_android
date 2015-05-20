package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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

        colorFilters = new ArrayList<>();
        patternFilters = new ArrayList<>();

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
                cards.add((CardView)itemView);
            }

            public CardView setAsAll() {
                id = -1;
                attributeLabel.setText("전체");
                CardView allCard = (CardView)itemView;
                allCard.setCardBackgroundColor(getResources().getColor(R.color.accent));

                return allCard;
            }

            @Override
            public void onClick(View view) {
                ArrayList<Integer> filters = (viewType == 0)? colorFilters : patternFilters;

                if (id == -1) {
                    if (!filters.isEmpty()) {
                        filters.clear();
                        for (CardView card : cards)
                            card.setCardBackgroundColor(0xFFFAFAFA);
                        allCard.setCardBackgroundColor(getResources().getColor(R.color.accent));
                    }
                    return;
                }

                if (filters.contains(id)) {
                    filters.remove(new Integer(id));
                    ((CardView)view).setCardBackgroundColor(0xFFFAFAFA);
                    if (filters.isEmpty())
                        allCard.setCardBackgroundColor(getResources().getColor(R.color.accent));
                }
                else {
                    filters.add(id);
                    ((CardView)view).setCardBackgroundColor(getResources().getColor(R.color.accent));
                    allCard.setCardBackgroundColor(0xFFFAFAFA);
                }


                // TODO: add filter or remove filter
                // TODO: changer view
            }
        }

        CardView allCard;
        ArrayList<CardView> cards;

        ArrayList<Pair<Integer, String>> attributeList;
        int viewType;

        public AttributeAdapter(int viewType) {
            this.viewType = viewType;
            attributeList = (viewType == 0)? SchemaData.getColorList() : SchemaData.getPatternList();
            cards = new ArrayList<>();
        }

        @Override
        public int getItemCount() {
            return attributeList.size() + 1;
        }

        @Override
        public void onBindViewHolder(AttributeHolder holder, int position) {
            if (position == 0) {
                allCard = holder.setAsAll();
                return;
            }

            holder.setView(attributeList.get(position - 1));
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
