package kr.ac.korea.ee.fit.request;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

import kr.ac.korea.ee.fit.model.User;

/**
 * Created by SHY_mini on 15. 6. 6..
 */
public class CollectionData extends PostData {

    String url;
    public static final int LIMIT = 2;

    public static CollectionData makeCollection(String name, String description) {
        CollectionData makeCollection = new CollectionData();

        makeCollection.url = "http://" + ipAddress + "/collection/make";

        makeCollection.add("user_id", User.getDeviceUserId());
        makeCollection.add("name", name);
        makeCollection.add("description", description);

        return makeCollection;
    }

    public static CollectionData getCollections(String user_id) {
        CollectionData getCollections = new CollectionData();

        getCollections.url = "http://" + ipAddress + "/collection/getCollections";

        getCollections.add("user_id", user_id);

        return getCollections;
    }

    public static CollectionData isLiked(int collectionId) {
        CollectionData isLiked = new CollectionData();

        isLiked.url = "http://" + ipAddress + "/collection/isLiked";

        isLiked.add("user_id", User.getDeviceUserId());
        isLiked.add("collection_id", collectionId);

        return isLiked;
    }

    public static CollectionData getPopular() {
        CollectionData getPopular = new CollectionData();

        getPopular.url = "http://" + ipAddress + "/collection/getPopular";

        return getPopular;
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
