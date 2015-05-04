package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class Event extends PostData {

    final String url = "http://" + ipAddress + "/eventServer/rate";

    public Event(String user_id, int fashion_id, int type_id) {
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("user_id", user_id));
        data.add(new BasicNameValuePair("fashion_id", String.valueOf(fashion_id)));
        data.add(new BasicNameValuePair("type_id", String.valueOf(type_id)));
    }

    @Override
    public String getURL() {
        return url;
    }
}
