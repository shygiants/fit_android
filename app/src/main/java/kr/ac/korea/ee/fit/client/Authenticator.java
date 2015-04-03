package kr.ac.korea.ee.fit.client;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import kr.ac.korea.ee.fit.model.Credential;

/**
 * Created by SHYBook_Air on 15. 4. 2..
 */
public class Authenticator extends HTTPClient<Credential> {

    @Override
    public void start (Credential credential) {
        url = credential.url;

        execute(credential);
    }
}
