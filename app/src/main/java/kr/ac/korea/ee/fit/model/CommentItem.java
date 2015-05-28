package kr.ac.korea.ee.fit.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.json.JSONObject;

/**
 * Created by SHYBook_Air on 15. 5. 12..
 */
public class CommentItem {

    Bitmap profile;
    String nickname;
    String comment;

    private boolean mSelectable = true;


    public CommentItem(JSONObject jsonObject) {

    }

    public CommentItem(String comment) {
        // TODO: add user's info
        this.comment = comment;
    }

    public CommentItem() {
        this.comment = "멋져요!";
        this.nickname = "누누";
    }

    public Bitmap getProfile() {
        return profile;
    }

    public String getNickname() {
        return nickname;
    }

    public String getComment() {
        return comment;
    }
}

