package kr.ac.korea.ee.fit.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.json.JSONObject;

/**
 * Created by SHYBook_Air on 15. 5. 12..
 */
public class Comment {

    int id;

    Bitmap profile;
    String nickname;

    String comment;
    String userId;
    int fashionId;
    String created;
    int numOfLikes;
    boolean userLikes;

    public Comment(JSONObject jsonObject) {
        try {
            nickname = jsonObject.getString("nick_name");
            comment = jsonObject.getString("comment_text");
            userId = jsonObject.getString("user_id");
            id = jsonObject.getInt("id");
            created = jsonObject.getString("created_date");
            numOfLikes = (jsonObject.getString("num_of_likes").equals("null"))? 0 : jsonObject.getInt("num_of_likes");
            userLikes = jsonObject.getString("userLikes").equals(User.getDeviceUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Comment(int fashion_id, String comment) {
        // TODO: add user's info
        userId = User.getDeviceUserId();
        nickname = User.getDeviceUser().getNickName();
        this.comment = comment;
        created = "방금";
        fashionId = fashion_id;
    }

    public Bitmap getProfile() {
        return profile;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getComment() {
        return comment;
    }

    public String getUserId() {
        return userId;
    }

    public String getCreated() {
        return created;
    }

    public int getFashionId() {
        return fashionId;
    }

    public int getNumOfLikes() { return numOfLikes; }

    public boolean userLikes() { return userLikes; }
}

