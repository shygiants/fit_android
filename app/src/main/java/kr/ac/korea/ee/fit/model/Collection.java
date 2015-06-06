package kr.ac.korea.ee.fit.model;

import android.graphics.Bitmap;

import org.json.JSONObject;

/**
 * Created by SHY_mini on 15. 6. 6..
 */
public class Collection {

    int collectionId;
    String userId;
    String collectionName;
    String collectionDesc;
    String thumbnailPath;
    Bitmap thumbnail;

    public Collection(int collectionId, String userId, String collectionName, Bitmap thumbnail) {
        this.collectionId = collectionId;
        this.userId = userId;
        this.collectionName = collectionName;
        this.thumbnail = thumbnail;
    }

    public Collection(JSONObject jsonObject) {
        try {
            collectionId = jsonObject.getInt("id");
            userId = jsonObject.getString("user_id");
            collectionName = jsonObject.getString("name");
            collectionDesc = jsonObject.getString("desc");
            thumbnailPath = jsonObject.getString("thumbnail");
            if (thumbnailPath.equals("null"))
                thumbnailPath = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Collection getRatedCollection(String userId, Bitmap thumbnail) {
        Collection ratedCollection = new Collection(0, userId, "좋아하는 패션", thumbnail);

        return ratedCollection;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getCollectionDesc() { return collectionDesc; }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public String getThumbnailPath() { return thumbnailPath; }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

}
