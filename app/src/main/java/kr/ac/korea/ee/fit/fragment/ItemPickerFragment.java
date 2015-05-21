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

/**
 * Created by SHYBook_Air on 15. 5. 17..
 */
public class ItemPickerFragment extends Fragment {

    ClassAdapter classAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        classAdapter = new ClassAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itempicker, container, false);

        RecyclerView classList = (RecyclerView)view.findViewById(R.id.classList);
        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        classList.setLayoutManager(linearLayoutManager);

        classList.setAdapter(classAdapter);

        return view;
    }

    private class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ItemListHolder> {

        public class ItemListHolder extends RecyclerView.ViewHolder {

            int id;
            RecyclerView itemList;
            TextView classLabel;

            public ItemListHolder(View view) {
                super(view);

                itemList = (RecyclerView)view.findViewById(R.id.itemList);
                classLabel = (TextView)view.findViewById(R.id.classLabel);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false);
                itemList.setLayoutManager(gridLayoutManager);
            }

            public void setView(Pair<Integer, String> classData) {
                id = classData.first;
                classLabel.setText(classData.second);
                itemList.setAdapter(new ItemAdapter(id));
            }
        }

        ArrayList<Pair<Integer, String>> classList;

        public ClassAdapter() {
            classList = SchemaData.getClassList();
        }

        @Override
        public int getItemCount() {
            return classList.size();
        }

        @Override
        public void onBindViewHolder(ItemListHolder holder, int position) {
            holder.setView(classList.get(position));
        }

        @Override
        public ItemListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.list_item, parent, false);

            return new ItemListHolder(view);
        }
    }


    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

        public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            int id;
            TextView itemText;

            public ItemHolder(View view) {
                super(view);
                itemText = (TextView)view.findViewById(R.id.itemText);
                view.setOnClickListener(this);
            }

            public void setView(Pair<Integer, String> itemType) {
                id = itemType.first;
                itemText.setText(itemType.second);
            }

            @Override
            public void onClick(View view) {
                DetailPickerFragment detailPickerFragment = new DetailPickerFragment();
                Bundle args = getArguments();
                args.putInt(DetailPickerFragment.TYPE_ID, id);
                args.putString(DetailPickerFragment.TYPE_LABEL, itemText.getText().toString());
                args.putBoolean(DetailPickerFragment.MODIFYING, false);

                detailPickerFragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.tabContainer, detailPickerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        int classId;
        ArrayList<Pair<Integer, String>> itemList;

        public ItemAdapter(int classId) {
            this.classId = classId;
            itemList = SchemaData.getItemTypeList(classId);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.setView(itemList.get(position));
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.card_item, parent, false);

            return new ItemHolder(view);
        }
    }
}
