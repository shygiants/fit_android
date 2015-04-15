package kr.ac.korea.ee.fit.model;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class Credential extends PostData {

    final String url = "http://" + ipAddress + "/authentication/login";

    protected Credential() {}

    public Credential(String email, String password) {
        data.add(new BasicNameValuePair("email", email));
        data.add(new BasicNameValuePair("password", password));
    }

    public Credential(String email, String password, String rePassword, String firstName, String lastName) {
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
