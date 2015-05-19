package kr.ac.korea.ee.fit.model;

import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.request.Schema;

/**
 * Created by SHYBook_Air on 15. 5. 19..
 */
public class SchemaData {

    static ArrayMap<Integer, ArrayList<Pair<Integer, String>>> itemTypeMap;
    static ArrayList<Pair<Integer, String>> classList;
    static ArrayList<Pair<Integer, String>> colorList;
    static ArrayList<Pair<Integer, String>> patternList;

    public static void Init() {
        classList = new ArrayList<>();
        itemTypeMap = new ArrayMap<>();
        colorList = new ArrayList<>();
        patternList = new ArrayList<>();
        GetterTask getter = new GetterTask();
        getter.start(Schema.schemaDataGetter());
    }

    public static ArrayList getItemTypeList(int id) {
        return itemTypeMap.get(id);
    }
    public static ArrayList getClassList() { return classList; }
    public static ArrayList getColorList() { return colorList; }
    public static ArrayList getPatternList() { return patternList; }

    private static class GetterTask extends HTTPClient<Schema> {

        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                JSONObject itemTypes = response.getJSONObject("item_types");
                JSONArray classes = response.getJSONArray("classes");
                JSONArray colors = response.getJSONArray("colors");
                JSONArray patterns = response.getJSONArray("patterns");

                int numOfClasses = classes.length();
                for (int i = 0; i < numOfClasses; i++) {
                    JSONObject class_json = classes.getJSONObject(i);
                    int id = class_json.getInt("id");
                    classList.add(new Pair<>(id, class_json.getString("label")));

                    itemTypeMap.put(id, new ArrayList<Pair<Integer, String>>());
                    JSONArray itemTypesWithClass = itemTypes.getJSONArray(String.valueOf(id));
                    int numOfItems = itemTypesWithClass.length();
                    for (int j = 0; j < numOfItems; j++) {
                        JSONObject type_json = itemTypesWithClass.getJSONObject(j);
                        itemTypeMap.get(id).add(new Pair<>(type_json.getInt("id"), type_json.getString("label")));
                    }
                }

                int numOfColors = colors.length();
                for (int i = 0; i < numOfColors; i++) {
                    JSONObject color = colors.getJSONObject(i);
                    colorList.add(new Pair<>(color.getInt("id"), color.getString("label")));
                }

                int numOfPatterns = patterns.length();
                for (int i = 0; i < numOfPatterns; i++) {
                    JSONObject pattern = patterns.getJSONObject(i);
                    patternList.add(new Pair<>(pattern.getInt("id"), pattern.getString("label")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
