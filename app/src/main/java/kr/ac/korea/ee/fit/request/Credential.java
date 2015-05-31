package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class Credential extends PostData {

    String url;

    public Credential(String email, String password) {
        url = "http://" + ipAddress + "/authentication/login";

        add("email", email);
        add("password", password);
    }

    public Credential(String email, String password, String firstName, String lastName) {
        url = "http://" + ipAddress + "/authentication/register";

        add("email", email);
        add("password", password);
        add("first_name", firstName);
        add("last_name", lastName);
    }

    @Override
    public String getURL() {
        return url;
    }

}
