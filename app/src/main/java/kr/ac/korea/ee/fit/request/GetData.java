package kr.ac.korea.ee.fit.request;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public abstract class GetData extends Request {

    @Override
    public String getMethod() {
        return "get";
    }
}
