package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 5. 28..
 */
public class GetUserData extends PostData {

    final String url = "http://" + ipAddress + "/user/getInfo";

    public GetUserData(String email) {
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("email", email));
    }

    @Override
    public String getURL() {
        return url;
    }
}
