package kr.ac.korea.ee.fit.request;

/**
 * Created by SHY_mini on 15. 4. 29..
 */
public class CheckLogin extends GetData {

    final String url = "http://" + ipAddress + "/authentication/checkLogin";

    @Override
    public String getURL() {
        return url;
    }
}
