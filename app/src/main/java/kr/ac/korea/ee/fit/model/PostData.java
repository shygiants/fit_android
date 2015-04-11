package kr.ac.korea.ee.fit.model;

import org.apache.http.NameValuePair;
import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public abstract class PostData extends ArrayList<NameValuePair> {

    final protected String ipAddress = "192.168.0.11:8080";
//    final protected String ipAddress = "192.168.0.26:8080"; // 학회실 SHYBook_Air
//    final protected String ipAddress = "163.152.21.217"; // 학회실 서버

    public String url;
    public String method;
}


