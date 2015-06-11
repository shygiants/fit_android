package kr.ac.korea.ee.fit.model;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class FashionCard {

    int fashionId;
    String editorName;
    String nickname;
    String vendorName;
    String imgPath;
    Bitmap image;
    int ratingType;
    String editorId;

    public FashionCard(int fashion_id, String last_name, String first_name, String img_path) {
        fashionId = fashion_id;
        editorName = last_name + first_name;
        imgPath = img_path;
    }

    public FashionCard(JSONObject jsonObject) {
        try {
            fashionId = jsonObject.getInt("id");
            editorName = jsonObject.getString("last_name") + jsonObject.getString("first_name");
            nickname = jsonObject.getString("nick_name");
            vendorName = jsonObject.getString("vendor_name");
            imgPath = jsonObject.getString("img_path");
            editorId = jsonObject.getString("email");
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

    public void setImage(Bitmap image) { this.image = image; }

    public void setRatingType(int ratingType) {
        this.ratingType = ratingType;
    }

    public Bitmap getImage() { return image; }

    public int getFashionId() {
        return fashionId;
    }

    public String getEditorName() {
        return editorName;
    }

    public String getNickname() { return nickname; }

    public String getImgPath() {
        return imgPath;
    }

    public int getRatingType() { return ratingType; }

    public String getVendorName() { return vendorName; }

    public String getEditorId() { return editorId; }
}
