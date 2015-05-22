package kr.ac.korea.ee.fit.request;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public abstract class Request {

//    final protected String ipAddress = "192.168.0.26:8080"; // 학회실 SHYBook_Air
    final protected String ipAddress = "172.30.36.215:8080";
//    final protected String ipAddress = "192.168.0.4:8080"; // 자취방 SHYBook_Air
//    final protected String ipAddress = "163.152.21.217"; // 학회실 서버

    public abstract String getURL();
    public abstract String getMethod();
}
