package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.model.User;

/**
 * Created by SHYBook_Air on 15. 5. 12..
 */
public class Detail extends PostData {

    final String url = "http://" + ipAddress + "/feed/getDetail";

    public Detail(String fashion_id) {
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("user_id", User.get().getEmail()));
        data.add(new BasicNameValuePair("fashion_id", fashion_id));
    }

    @Override
    public String getURL() {
        return url;
    }
}
