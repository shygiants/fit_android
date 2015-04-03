package kr.ac.korea.ee.fit.model;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class Credential extends PostData {

    public Credential(String email, String password)
    {
        add(new BasicNameValuePair("email", email));
        add(new BasicNameValuePair("password", password));

        url = "http://" + ipAddress + "/index.php/authentication/login";
    }

    public Credential(String email, String password, String rePassword, String firstName, String lastName)
    {
        add(new BasicNameValuePair("email", email));
        add(new BasicNameValuePair("password", password));
        add(new BasicNameValuePair("rePassword", rePassword));
        add(new BasicNameValuePair("firstName", firstName));
        add(new BasicNameValuePair("lastName", lastName));

        url = "http://" + ipAddress + "/index.php/authentication/register";
    }
}
