package kr.ac.korea.ee.fit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.ac.korea.ee.fit.R;

/**
 * Created by SHYBook_Air on 15. 5. 11..
 */
public class DetailFragment extends Fragment {

    public static final String FASHION_ID = "FashionID";
    int fashionId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        fashionId = getArguments().getInt(FASHION_ID);
        ((TextView)view.findViewById(R.id.fashionIdText)).setText(String.valueOf(fashionId));
        view.findViewById(R.id.detailButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailView(fashionId + 1);
            }
        });
        return view;
    }

    void startDetailView(int editorId) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle arg = new Bundle();
        arg.putInt(DetailFragment.FASHION_ID, editorId);
        detailFragment.setArguments(arg);

        getParentFragment().getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.tab, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
