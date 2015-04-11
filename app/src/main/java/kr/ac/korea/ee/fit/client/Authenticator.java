package kr.ac.korea.ee.fit.client;

import android.util.Log;

import java.net.URL;

import kr.ac.korea.ee.fit.model.Credential;

/**
 * Created by SHYBook_Air on 15. 4. 2..
 */
public class Authenticator extends HTTPClient<Credential> {

    @Override
    public void start (Credential credential) {

        try {
            url = new URL(credential.url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Authenticator", "Exception");
        }

        execute(credential);
    }
}
