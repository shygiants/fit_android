package kr.ac.korea.ee.fit.model;

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
    ImageView profile;
    int following;
    int follower;
    int rating;

    static User user;

    public static User get() {
        if (user != null)
            return user;
        createUser("NULL");
        return user;
    }

    public static void createUser(String email) {
        user = new User();
        user.email = email;
        user.getUserData();
    }

    public String getEmail() {
        return email;
    }

    void getUserData() {
        UserGetterTask getter = new UserGetterTask();
        getter.start(new GetUserData(email));
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
