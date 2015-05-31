package kr.ac.korea.ee.fit.request;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public abstract class Request {


//    static final protected String ipAddress = "192.168.0.26:8080"; // 학회실 SHYBook_Air
    static final protected String ipAddress = "172.30.10.146:8080";
//    static final protected String ipAddress = "192.168.0.4:8080"; // 자취방 SHYBook_Air
//    static final protected String ipAddress = "163.152.21.217"; // 학회실 서버
//    static final protected String ipAddress = "52.68.43.235";

    public abstract String getURL();
    public abstract String getMethod();
}
