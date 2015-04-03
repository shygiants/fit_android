package kr.ac.korea.ee.fit.client;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import kr.ac.korea.ee.fit.model.PostData;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public abstract class HTTPClient<T extends PostData> extends AsyncTask<T, Void, JSONObject>{

    protected String url;

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(T... params) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject responseJson = null;

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params[0], "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            responseJson = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));

            return responseJson;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("Authenticator", "ClientProtocolException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Authenticator", "IOException");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Authenticator", "JSONException");
            Log.e("Authenticator", "Response Text - " + responseJson);
        }

        return null;
    }

    public abstract void start(T params);
}
