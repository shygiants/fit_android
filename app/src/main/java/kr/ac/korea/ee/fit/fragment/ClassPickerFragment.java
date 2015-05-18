package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.model.SchemaData;
import kr.ac.korea.ee.fit.request.Schema;

/**
 * Created by SHYBook_Air on 15. 5. 17..
 */
public class ClassPickerFragment extends Fragment {

    ClassAdapter classAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        classAdapter = new ClassAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classpicker, container, false);

        RecyclerView itemList = (RecyclerView)view.findViewById(R.id.classList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        itemList.setLayoutManager(linearLayoutManager);

        itemList.setAdapter(classAdapter);

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
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                itemList.setLayoutManager(linearLayoutManager);
                itemList.setAdapter(new ItemAdapter(null));
            }

            public void setView(Pair<Integer, String> classData) {
                id = classData.first;
                classLabel.setText(classData.second);
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

        public class ItemHolder extends RecyclerView.ViewHolder {

            TextView itemText;

            public ItemHolder(View view) {
                super(view);
                itemText = (TextView)view.findViewById(R.id.itemText);
            }

            public void setView(String item) {
                itemText.setText(item);
            }
        }

        String _class;

        public ItemAdapter(String _class) {
            this._class = _class;
        }

        @Override
        public int getItemCount() {
            return 7;
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.setView(String.valueOf(position));
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
