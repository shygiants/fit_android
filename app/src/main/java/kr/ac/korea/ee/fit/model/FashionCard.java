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
}
