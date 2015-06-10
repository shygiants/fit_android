package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.model.Comment;
import kr.ac.korea.ee.fit.model.User;

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

    public static Event follow(String followed_id) {
        Event followEvent = new Event();

        followEvent.url = "http://" + ipAddress + "/eventServer/follow";

        followEvent.add("follower_id", User.getDeviceUserId());
        followEvent.add("followed_id", followed_id);

        return followEvent;
    }

    public static Event likeComment(int comment_id) {
        Event likeComment = new Event();

        likeComment.url = "http://" + ipAddress + "/eventServer/likeComment";

        likeComment.add("user_id", User.getDeviceUserId());
        likeComment.add("comment_id", comment_id);

        return likeComment;
    }

    public static Event collect(int rateId, int collectionId) {
        Event collect = new Event();

        collect.url = "http://" + ipAddress + "/eventServer/collect";

        collect.add("event_id", rateId);
        collect.add("collection_id", collectionId);

        return collect;
    }

    @Override
    public String getURL() {
        return url;
    }
}
