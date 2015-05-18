package kr.ac.korea.ee.fit.request;

/**
 * Created by SHYBook_Air on 15. 5. 19..
 */
public class Schema extends GetData {

    String url;

    public static Schema classDataGetter() {
        Schema classDataGetter = new Schema();
        classDataGetter.url = "http://" + classDataGetter.ipAddress + "/feed/getClass";

        return classDataGetter;
    }

    @Override
    public String getURL() {
        return url;
    }
}
