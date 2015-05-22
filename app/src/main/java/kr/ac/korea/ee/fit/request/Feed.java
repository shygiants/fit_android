package kr.ac.korea.ee.fit.request;

import android.util.Log;

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

    String url;

    static Feed feed;

    public static Feed getFeed() {
        if (feed != null)
            return feed;

        feed = new Feed();
        feed.url = "http://" + feed.ipAddress + "/feed/getAll";
        feed.data = new ArrayList<>();
        feed.data.add(new BasicNameValuePair("email", User.get().getEmail()));
        return feed;
    }

    Feed() {
        super();
    }

    public Feed (ArrayList<Filter> filters) {

        url = "http://" + ipAddress + "/feed/getFiltered";

        data = new ArrayList<>();


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

        data.add(new BasicNameValuePair("filters", filters_json.toString()));
        data.add(new BasicNameValuePair("email", User.get().getEmail()));
    }

    @Override
    public String getURL() {
        return url;
    }
}
