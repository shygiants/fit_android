package kr.ac.korea.ee.fit.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public abstract class PostData extends Request {

    protected ArrayList<NameValuePair> data = new ArrayList<>();

    @Override
    public String getMethod() {
        return "post";
    }

    protected void add(String name, String value) {
        data.add(new BasicNameValuePair(name, value));
    }

    protected void add(String name, int value) {
        data.add(new BasicNameValuePair(name, String.valueOf(value)));
    }

    public ArrayList<NameValuePair> getData() {
        return data;
    }
}


