package kr.ac.korea.ee.fit.request;

import org.apache.http.NameValuePair;
import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public abstract class PostData extends Request {

    protected ArrayList<NameValuePair> data;

    @Override
    public String getMethod() {
        return "post";
    }

    public ArrayList<NameValuePair> getData() {
        return data;
    }
}


