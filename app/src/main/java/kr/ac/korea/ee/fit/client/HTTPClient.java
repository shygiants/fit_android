package kr.ac.korea.ee.fit.client;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import kr.ac.korea.ee.fit.request.PostData;
import kr.ac.korea.ee.fit.request.Request;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class HTTPClient<T extends Request> extends AsyncTask<T, Void, JSONObject>{

    protected URL url;

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

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);

            if (params[0].getMethod() == "post") {
                connection.setDoOutput(true);

                OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                writer.write(postToString((PostData)params[0]));
                writer.flush();
                writer.close();
                outputStream.close();
            }

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line+"\n");

            reader.close();
            inputStream.close();

            connection.disconnect();

            JSONObject responseJson = new JSONObject(stringBuilder.toString());
            return responseJson;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e("HTTPClient", "IOException");
        } catch (JSONException je) {
            je.printStackTrace();
            Log.e("HTTPClient", "JSONException");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTPClient", "Exception");
        }

        return null;
    }

    private String postToString(PostData params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;

        for (NameValuePair pair : params.getData())
        {
            if (isFirst)
                isFirst = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public void start(T params) {

        try {
            url = new URL(params.getURL());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTPClient", "Exception");
        }

        execute(params);
    }
}
