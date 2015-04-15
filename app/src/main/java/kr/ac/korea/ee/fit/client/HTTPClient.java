package kr.ac.korea.ee.fit.client;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import kr.ac.korea.ee.fit.model.PostData;
import kr.ac.korea.ee.fit.model.Request;

/**
 * Created by SHYBook_Air on 15. 4. 3..
 */
public class HTTPClient<T extends Request> extends AsyncTask<T, Void, JSONObject>{

    protected URL url;

    final public static String PUT = "Cookie";
    final public static String GET = "Set-Cookie";

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

//            String cookie = "null";
//            Map m = connection.getHeaderFields();
//            if (m.containsKey(GET)) {
//                Collection c =(Collection)m.get(GET);
//                for(Iterator i = c.iterator(); i.hasNext(); ) {
//                    cookie = (String)i.next();
//                }
//                Log.i("Cookie", cookie);
//            }

            connection.disconnect();

            JSONObject responseJson = new JSONObject(stringBuilder.toString());

            return responseJson;

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
