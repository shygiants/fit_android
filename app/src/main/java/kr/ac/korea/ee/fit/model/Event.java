package kr.ac.korea.ee.fit.model;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class Event extends PostData {

    final String url = "http://" + ipAddress + "/eventServer/rate";

    public Event(String userId, String rating, String itemId) {
        data.add(new BasicNameValuePair("userId", userId));
        data.add(new BasicNameValuePair("rating", rating));
        data.add(new BasicNameValuePair("itemId", itemId));
    }

    @Override
    public String getURL() {
        return url;
    }
}
