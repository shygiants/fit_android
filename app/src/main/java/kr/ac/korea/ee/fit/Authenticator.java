package kr.ac.korea.ee.fit;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 4. 2..
 */
public class Authenticator extends AsyncTask<String, Void, String> {

    private String url = "http://192.168.0.26:8080/index.php/authentication/register";
//    private String url = "http://163.152.21.217/index.php/authentication/register";

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        ArrayList<NameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair("email", "shygiants@nate.com"));
        postData.add(new BasicNameValuePair("password", "12345678"));
        postData.add(new BasicNameValuePair("re_password", "12345678"));
        postData.add(new BasicNameValuePair("firstName", "Sanghun"));
        postData.add(new BasicNameValuePair("lastName", "Yun"));

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        String responseText = null;

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, "UTF-8");
            httpPost.setEntity(entity);

            HttpResponse httpResponse = httpClient.execute(httpPost);

            responseText = EntityUtils.toString(httpResponse.getEntity());
            JSONObject responseJson = new JSONObject(responseText);
            return responseJson.getString("success");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("Authenticator", "ClientProtocolException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Authenticator", "IOException");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Authenticator", "JSONException");
            Log.e("Authenticator", "Response Text - " + responseText);
        }

        return null;
    }
}
