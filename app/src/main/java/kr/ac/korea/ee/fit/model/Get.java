package kr.ac.korea.ee.fit.model;

/**
 * Created by SHYBook_Air on 15. 4. 11..
 */
public class Get extends Credential {

    public Get() {


        url = "http://" + ipAddress + "/authentication/checkLogin";
        method = "get";
    }
}
