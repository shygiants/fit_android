package kr.ac.korea.ee.fit.model;

import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import kr.ac.korea.ee.fit.R;

/**
 * Created by SHYBook_Air on 15. 5. 12..
 */
public class Fashion {

    String srcLink;
    String gender;
    String editorName;
    String season;
    String style;
    String age;
    String createDate;
    String rate;

    public Fashion(JSONObject fashion) {
        try {
            srcLink = fashion.getString("src_link");
            gender = fashion.getString("gender_label");
            editorName = fashion.getString("first_name") + fashion.getString("last_name");
            season = fashion.getString("season_label");
            style = fashion.getString("style_label");
            age = fashion.getString("age_label");
            createDate = fashion.getString("created_date");
            rate = fashion.getString("type_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View setView(View view) {
        ((TextView)view.findViewById(R.id.srcLinkText)).setText(srcLink);

        return view;
    }
}
