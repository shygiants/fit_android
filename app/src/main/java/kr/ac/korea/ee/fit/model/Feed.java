package kr.ac.korea.ee.fit.model;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class Feed extends GetData {

    final String url = "http://" + ipAddress + "/feed/getAll";

    static Feed feed;

    public static Feed getFeed() {
        if (feed != null)
            return feed;

        feed = new Feed();
        return feed;
    }

    @Override
    public String getURL() {
        return url;
    }
}
