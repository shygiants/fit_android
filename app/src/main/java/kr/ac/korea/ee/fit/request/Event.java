package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.model.Comment;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class Event extends PostData {

    String url;

    public static Event rate(String user_id, int fashion_id, int type_id) {
        Event rateEvent = new Event();

        rateEvent.url = "http://" + ipAddress + "/eventServer/rate";

        rateEvent.add("user_id", user_id);
        rateEvent.add("fashion_id", fashion_id);
        rateEvent.add("type_id", type_id);

        return rateEvent;
    }

    public static Event comment(Comment comment) {
        Event commentEvent = new Event();

        commentEvent.url = "http://" + ipAddress + "/eventServer/comment";

        commentEvent.add("user_id", comment.getUserId());
        commentEvent.add("fashion_id", comment.getFashionId());
        commentEvent.add("comment_text", comment.getComment());

        return commentEvent;
    }

    public static Event getComments(int fashion_id) {
        Event getComments = new Event();

        getComments.url = "http://" + ipAddress + "/feed/getComments";

        getComments.add("fashion_id", fashion_id);

        return getComments;
    }

    @Override
    public String getURL() {
        return url;
    }
}
