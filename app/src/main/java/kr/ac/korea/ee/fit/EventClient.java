package kr.ac.korea.ee.fit;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by SHYBook_Air on 15. 3. 26..
 */

// TODO HttpClient is deprecated. Let's use HttpURLConnection

class EventClient extends AsyncTask<String, Void, String>
{
    private String path = "http://163.152.21.217:7070/events.json"; // Server URL
//    private String path = "http://ec2-52-68-31-132.ap-northeast-1.compute.amazonaws.com:7070/events.json";
    private String accessKey = "wERSmq7bExLqaTR9FyJKMJtEOTu0ikH74Sf4ovyLc8G3vWGNsSX2NCa29YVshWLu"; // fit App Access Key
//    private String accessKey = "H9khIYL0sNVzWdJIbKrDN90Nkf7Jjhk9VoPejZkQrUfmneosA2Jq4cGr5KBtHo1G";

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
        try {
            HttpPost httpPost = new HttpPost(path + "?accessKey=" + accessKey);
            httpPost.setHeader("Content-Type", "application/json");
            StringEntity se = new StringEntity(getJsonEvent(params[0]).toString());
            se.setContentType("application/json");
            httpPost.setEntity(se);

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);

            String responseText = EntityUtils.toString(httpResponse.getEntity());
            JSONObject responseJson = new JSONObject(responseText);
            return responseJson.getString("eventId");
        } catch (JSONException jsone) {
            Log.e("Event Client", "JSON Exception");
            return null;
        } catch (Exception e) {
            Log.e("Event Client", "Network Exception");
            return null;
        }
    }

    protected JSONObject getJsonEvent(String event) throws JSONException
    {
        JSONObject eventJson = new JSONObject();
        eventJson.put("event", event);
        eventJson.put("entityType", "user");
        eventJson.put("entityId", "1");
        eventJson.put("targetEntityType", "item");
        eventJson.put("targetEntityId", "1");

        JSONObject properties = new JSONObject();
        properties = properties.put("rating", "3");
        eventJson.put("properties", properties);

        return eventJson;
    }
}

class RateEvent extends EventClient {
//    private String url = "http://163.152.21.217/eventserver/rate";
    private String url = "http://192.168.0.26:8080/index.php/eventserver/rate";
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

        String rating = params[0];
        ArrayList<NameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair("userId", "shygiants"));
        postData.add(new BasicNameValuePair("rating", rating));
        postData.add(new BasicNameValuePair("itemId", "item1"));

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, "UTF-8");
            httpPost.setEntity(entity);

            HttpResponse httpResponse = httpClient.execute(httpPost);

            String responseText = EntityUtils.toString(httpResponse.getEntity());
            JSONObject responseJson = new JSONObject(responseText);
            return responseJson.getString("eventId");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("RateClient", "ClientProtocolException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RateClient", "IOException");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("RateClient", "JSONException");
        }

        return null;
    }
}
