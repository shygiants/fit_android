package kr.ac.korea.ee.fit.model;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class Event extends PostData {

    public Event(String userId, String rating, String itemId)
    {
        add(new BasicNameValuePair("userId", userId));
        add(new BasicNameValuePair("rating", rating));
        add(new BasicNameValuePair("itemId", itemId));

        url = "http://" + ipAddress + "/index.php/eventServer/rate";
    }
}
