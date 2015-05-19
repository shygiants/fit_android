package kr.ac.korea.ee.fit.request;

/**
 * Created by SHYBook_Air on 15. 5. 19..
 */
public class Schema extends GetData {

    String url;

    public static Schema schemaDataGetter() {
        Schema getter = new Schema();
        getter.url = "http://" + getter.ipAddress + "/feed/getSchemaData";

        return getter;
    }

    @Override
    public String getURL() {
        return url;
    }
}
