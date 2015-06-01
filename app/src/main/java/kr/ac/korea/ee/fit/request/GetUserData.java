package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.model.User;

/**
 * Created by SHYBook_Air on 15. 5. 28..
 */
public class GetUserData extends PostData {

    final String url = "http://" + ipAddress + "/user/getInfo";

    public GetUserData(String email) {
        add("email", email);
        add("viewer_id", User.getDeviceUserId());
    }

    @Override
    public String getURL() {
        return url;
    }
}
