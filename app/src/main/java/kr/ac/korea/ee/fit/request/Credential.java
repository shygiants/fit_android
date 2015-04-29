package kr.ac.korea.ee.fit.request;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class Credential extends PostData {

    final String url = "http://" + ipAddress + "/authentication/login";

    public Credential(String email, String password) {
        data = new ArrayList<>();

        data.add(new BasicNameValuePair("email", email));
        data.add(new BasicNameValuePair("password", password));
    }

    public Credential(String email, String password, String rePassword, String firstName, String lastName) {
        data = new ArrayList<>();

        data.add(new BasicNameValuePair("email", email));
        data.add(new BasicNameValuePair("password", password));
        data.add(new BasicNameValuePair("re_password", rePassword));
        data.add(new BasicNameValuePair("first_name", firstName));
        data.add(new BasicNameValuePair("last_name", lastName));
    }

    @Override
    public String getURL() {
        return url;
    }

}
