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
    String nickname;
    String editorId;
    String vendorName;
    String season;
    String style;
    String age;
    String createDate;
    int rate;
    int rateId;
    int ratingType;
    boolean following;

    public Fashion(JSONObject fashion) {
        try {
            srcLink = fashion.getString("src_link");
            gender = fashion.getString("gender_label");
            editorName = fashion.getString("last_name") + fashion.getString("first_name");
            nickname = fashion.getString("nick_name");
            editorId = fashion.getString("email");
            vendorName = fashion.getString("vendor_name");
            season = fashion.getString("season_label");
            style = fashion.getString("style_label");
            age = fashion.getString("age_label");
            createDate = fashion.getString("created_date");
            rate = fashion.getInt("type_id");
            rateId = fashion.getInt("rate_id");

            following = fashion.getString("follower_id").equals(User.getDeviceUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEditorName() {
        return editorName;
    }

    public String getEditorId() { return editorId; }

    public int getRate() {
        return rate;
    }

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public String getSrcLink() {
        return srcLink;
    }

    public boolean getFollowing() { return following; }

    public String getNickname() { return nickname; }

    public String getVendorName() { return vendorName; }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
