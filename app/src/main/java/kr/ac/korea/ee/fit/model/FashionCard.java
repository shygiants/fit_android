package kr.ac.korea.ee.fit.model;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class FashionCard {

    int fashionId;
    String editorName;
    String imgPath;
    int ratingType;

    public FashionCard(int fashion_id, String last_name, String first_name, String img_path) {
        fashionId = fashion_id;
        editorName = last_name + first_name;
        imgPath = img_path;
    }

    public FashionCard(JSONObject jsonObject) {
        try {
            fashionId = jsonObject.getInt("id");
            editorName = jsonObject.getString("last_name") + jsonObject.getString("first_name");
            imgPath = jsonObject.getString("img_path");
            String ratingStr = jsonObject.getString("type_id");

            switch (ratingStr) {
                case "1":
                    ratingType = 1;
                    break;
                case "2":
                    ratingType = 2;
                    break;
                case "3":
                    ratingType = 3;
                    break;
                default:
                    ratingType = 0;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FashionCard", "Constructor Exception");
        }
    }

    public int getFashionId() {
        return fashionId;
    }

    public String getEditorName() {
        return editorName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public int getRatingType() { return ratingType; }
}
