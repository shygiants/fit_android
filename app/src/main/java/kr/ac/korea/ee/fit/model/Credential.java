package kr.ac.korea.ee.fit.model;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class Credential extends PostData {

    protected Credential() {}

    public Credential(String email, String password)
    {
        add(new BasicNameValuePair("email", email));
        add(new BasicNameValuePair("password", password));

        url = "http://" + ipAddress + "/authentication/login";
        method = "post";
    }

    public Credential(String email, String password, String rePassword, String firstName, String lastName)
    {
        add(new BasicNameValuePair("email", email));
        add(new BasicNameValuePair("password", password));
        add(new BasicNameValuePair("re_password", rePassword));
        add(new BasicNameValuePair("first_name", firstName));
        add(new BasicNameValuePair("last_name", lastName));

        url = "http://" + ipAddress + "/authentication/register";
        method = "post";
    }
}
