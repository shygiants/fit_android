package kr.ac.korea.ee.fit.model;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.request.Schema;

/**
 * Created by SHYBook_Air on 15. 5. 19..
 */
public class SchemaData {

    static ArrayList<Pair<Integer, String>> classList;

    public static void Init() {
        classList = new ArrayList<>();
        ClassGetTask getClass = new ClassGetTask();
        getClass.start(Schema.classDataGetter());
    }

    public static ArrayList getClassList() {
        return classList;
    }

    private static class ClassGetTask extends HTTPClient<Schema> {

        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                JSONArray classes = response.getJSONArray("classes");
                int arraySize = classes.length();
                for (int i = 0; i < arraySize; i++) {
                    JSONObject class_json = classes.getJSONObject(i);
                    classList.add(new Pair<>(new Integer(class_json.getInt("id")), class_json.getString("label")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
