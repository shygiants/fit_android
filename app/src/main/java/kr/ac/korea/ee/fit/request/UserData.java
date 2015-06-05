package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.model.User;

/**
 * Created by SHYBook_Air on 15. 5. 28..
 */
public class UserData extends PostData {

    String url;

    public static UserData getUserData(String email) {
        UserData getUserData = new UserData();

        getUserData.url = "http://" + ipAddress + "/user/getInfo";

        getUserData.add("email", email);
        getUserData.add("viewer_id", User.getDeviceUserId());

        return getUserData;
    }

    public static UserData updateNickName(String nickName) {
        UserData updateNickName = new UserData();

        updateNickName.url = "http://" + ipAddress + "/user/updateNickName";

        updateNickName.add("email", User.getDeviceUserId());
        updateNickName.add("nick_name", nickName);

        return updateNickName;
    }

    @Override
    public String getURL() {
        return url;
    }
}
