package kr.ac.korea.ee.fit.model;

import android.graphics.Bitmap;

import org.json.JSONObject;

import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.request.UserData;

/**
 * Created by SHYBook_Air on 15. 5. 4..
 */
public class User {

    String email;

    String nickName;
    String firstName;
    String lastName;
    int following;
    int follower;
    int rating;

    boolean isFollowing;

    Bitmap profile;

    static User deviceUser;

    private User(String email) {
        this.email = email;
    }

    public User(JSONObject result) {
        try {
            email = result.getString("email");
            nickName = result.getString("nick_name");
            firstName = result.getString("first_name");
            lastName = result.getString("last_name");
            following = result.getInt("following");
            follower = result.getInt("follower");
            rating = result.getInt("rating");
            isFollowing = result.getBoolean("is_following");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createDeviceUser(String email) {
        deviceUser = new User(email);
        deviceUser.getUserData();
    }

    public static User getDeviceUser() {
        return deviceUser;
    }

    public static String getDeviceUserId() {
        return deviceUser.email;
    }

    public static boolean isDeviceUser(String userId) {
        return (deviceUser.email.equals(userId));
    }

    void getUserData() {
        UserGetterTask getter = new UserGetterTask();
        getter.start(UserData.getUserData(email));
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String getName() {
        return lastName + firstName;
    }

    public int getFollowing() {
        return following;
    }

    public int getFollower() {
        return follower;
    }

    public int getRating() {
        return rating;
    }

    public boolean isFollowing() { return isFollowing; }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    private class UserGetterTask extends HTTPClient<UserData> {
        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                email = result.getString("email");
                nickName = result.getString("nick_name");
                firstName = result.getString("first_name");
                lastName = result.getString("last_name");
                following = result.getInt("following");
                follower = result.getInt("follower");
                rating = result.getInt("rating");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
