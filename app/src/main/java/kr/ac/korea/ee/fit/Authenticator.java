package kr.ac.korea.ee.fit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.model.SchemaData;
import kr.ac.korea.ee.fit.model.User;
import kr.ac.korea.ee.fit.request.CheckLogin;
import kr.ac.korea.ee.fit.request.Credential;
import kr.ac.korea.ee.fit.request.GetData;

/**
 * Created by SHY_mini on 15. 4. 29..
 */
public class Authenticator {

    public final static String ACCOUNT = "ACCOUNT";
    public final static String EMAIL = "EMAIL";
    public final static String PASSWORD = "PASSWORD";

    static SharedPreferences store = null;

    public static Credential getSavedAccount(Context context) {
        if (store == null)
            store = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);

        if (!store.contains(EMAIL) || !store.contains(PASSWORD))
            return null;

        String email = store.getString(EMAIL, null);
        String password = store.getString(PASSWORD, null);

        return new Credential(email, password);
    }

    public static void onLogin(Context context) {
        if (store == null)
            store = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);

        User.createDeviceUser(store.getString(EMAIL, null));
        SchemaData.Init();
    }

    public static void saveAccount(Context context, String email, String password) {
        if (store == null)
            store = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = store.edit();

        editor.clear();
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.apply();
        onLogin(context);
    }

    public static void deleteAccount(Context context) {
        if (store == null)
            store = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);

        store.edit().clear().apply();
    }
}

