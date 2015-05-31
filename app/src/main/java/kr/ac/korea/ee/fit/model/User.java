package kr.ac.korea.ee.fit.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import org.json.JSONObject;

import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.request.GetUserData;

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

    Bitmap profile;

    static User deviceUser;

    public static void createDeviceUser(String email) {
        deviceUser = new User();
        deviceUser.email = email;
        deviceUser.getUserData();
    }

    public static User getDeviceUser() {
        return deviceUser;
    }

    public static String getDeviceUserId() {
        return deviceUser.email;
    }

    void getUserData() {
        UserGetterTask getter = new UserGetterTask();
        getter.start(new GetUserData(email));
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

    private class UserGetterTask extends HTTPClient<GetUserData> {
        @Override
        protected void onPostExecute(JSONObject result) {
            try {
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
