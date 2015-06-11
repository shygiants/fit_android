package kr.ac.korea.ee.fit.request;

import kr.ac.korea.ee.fit.model.User;

/**
 * Created by SHY_mini on 15. 6. 6..
 */
public class CollectionData extends PostData {

    String url;

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

    @Override
    public String getURL() {
        return url;
    }
}
