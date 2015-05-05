package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.model.User;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class Feed extends PostData {

    final String url = "http://" + ipAddress + "/feed/getAll";

    static Feed feed;

    public static Feed getFeed() {
        if (feed != null)
            return feed;

        feed = new Feed();
        feed.data = new ArrayList<>();
        feed.data.add(new BasicNameValuePair("email", User.get().getEmail()));
        return feed;
    }

    @Override
    public String getURL() {
        return url;
    }
}
