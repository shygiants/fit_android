package kr.ac.korea.ee.fit.request;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.model.Filter;
import kr.ac.korea.ee.fit.model.User;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class Feed extends PostData {

    public int LIMIT = 5;

    String url;

    public static Feed getFeed() {
        Feed feed = new Feed();
        feed.url = "http://" + ipAddress + "/feed/getAll";
        feed.add("email", User.getDeviceUserId());
        return feed;
    }

    public static Feed getRecommended() {
        Feed feed = new Feed();
        feed.url = "http://" + ipAddress + "/feed/getRecommended";

        feed.add("user_id", User.getDeviceUserId());

        feed.LIMIT = 3;

        return feed;
    }

    public static Feed getFiltered(ArrayList<Filter> filters) {
        Feed feed = new Feed();
        feed.url = "http://" + ipAddress + "/feed/getFiltered";

        JSONArray filters_json = new JSONArray();

        try {
            for (Filter filter : filters) {
                JSONObject filter_json = new JSONObject();
                filter_json.put("type_id", filter.getTypeId());
                JSONArray colors = new JSONArray(filter.getColors());
                JSONArray patterns = new JSONArray(filter.getPatterns());
                filter_json.put("colors", colors);
                filter_json.put("patterns", patterns);
                filters_json.put(filter_json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Feed", filters_json.toString());
        }

        feed.add("filters", filters_json.toString());
        feed.add("email", User.getDeviceUserId());

        return feed;
    }

    static Feed getRated(String user_id) {
        Feed feed = new Feed();
        feed.url = "http://" + ipAddress + "/feed/getRated";

        feed.add("email", user_id);
        feed.add("viewer", User.getDeviceUserId());

        return feed;
    }

    public static Feed getCollection(int collection_id, String user_id) {
        if (collection_id == 0) {
            return getRated(user_id);
        }

        Feed getCollection = new Feed();

        getCollection.url = "http://" + ipAddress + "/feed/getCollection";

        getCollection.add("user_id", User.getDeviceUserId());
        getCollection.add("collection_id", collection_id);

        return getCollection;
    }

    public static Feed getDetail(int fashion_id) {
        Feed getDetail = new Feed();

        getDetail.url = "http://" + ipAddress + "/feed/getDetail";

        getDetail.add("user_id", User.getDeviceUserId());
        getDetail.add("fashion_id", fashion_id);

        return getDetail;
    }

    public static Feed getComments(int fashion_id) {
        Feed getComments = new Feed();

        getComments.url = "http://" + ipAddress + "/feed/getComments";

        getComments.add("fashion_id", fashion_id);
        getComments.add("user_id", User.getDeviceUserId());

        return getComments;
    }

    public void setPage(int page) {

        ArrayList<NameValuePair> buffer = new ArrayList<>();

        for (NameValuePair pair : data) {
            String name = pair.getName();
            if (name.equals("offset") || name.equals("limit"))
                buffer.add(pair);
        }

        for (NameValuePair pair : buffer) {
            data.remove(pair);
        }

        add("offset", LIMIT * page);
        add("limit", LIMIT);
    }

    @Override
    public String getURL() {
        return url;
    }
}
