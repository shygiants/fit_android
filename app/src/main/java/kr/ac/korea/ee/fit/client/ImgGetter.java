package kr.ac.korea.ee.fit.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by SHYBook_Air on 15. 4. 15..
 */
public class ImgGetter extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
