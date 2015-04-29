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
import kr.ac.korea.ee.fit.request.CheckLogin;
import kr.ac.korea.ee.fit.request.Credential;
import kr.ac.korea.ee.fit.request.GetData;

/**
 * Created by SHY_mini on 15. 4. 29..
 */
public class Authenticator {

    public final static String IS_LOGIN = "IS_LOGIN";
    public final static String HAS_ACCOUNT = "HAS_ACCOUNT";
    public final static String KEY_ERROR_MESSAGE = "ERR_MSG";
    public final static String ACCOUNT = "ACCOUNT";
    public final static String EMAIL = "EMAIL";
    public final static String PASSWORD = "PASSWORD";

    static Authenticator authenticator;
    Context context;
    SharedPreferences storage;

    public static Authenticator get(Context context) {
        if (authenticator != null) {
            authenticator.context = context;
            authenticator.storage = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);
            return authenticator;
        }

        authenticator = new Authenticator();
        authenticator.context = context;
        authenticator.storage = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);
        return authenticator;
    }

    public Bundle isLogin() {
        HTTPClient<CheckLogin> checkLogin = new HTTPClient<>();
        CheckLogin request = new CheckLogin();
        checkLogin.start(request);

        JSONObject response = null;
        Bundle result = new Bundle();
        try {
            response = checkLogin.get();
            if (response == null) throw new Exception();
            result.putBoolean(IS_LOGIN, (response.getString("is_login").equals("true")));
            result.putBoolean(HAS_ACCOUNT, storage.contains(EMAIL) && storage.contains(PASSWORD));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "네트워크에 문제가 있습니다", Toast.LENGTH_SHORT).show();
        }

        result.putString(KEY_ERROR_MESSAGE, "Network");
        return result;
    }

    public Bundle signIn() {
        String email = storage.getString(EMAIL, null);
        String password = storage.getString(PASSWORD, null);

        return signIn(email, password);
    }

    public Bundle signIn(String email, String password) {
        Credential credential = new Credential(email, password);
        HTTPClient<Credential> signIn = new HTTPClient<>();
        signIn.start(credential);

        JSONObject response = null;
        Bundle result = new Bundle();
        try {
            response = signIn.get();
            if (response == null) throw new Exception();
            boolean is_login = response.getString("is_login").equals("true");
            result.putBoolean(IS_LOGIN, (is_login));
            if (is_login) saveAccount(email, password);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "네트워크에 문제가 있습니다", Toast.LENGTH_SHORT).show();
        }

        result.putString(KEY_ERROR_MESSAGE, "Network");
        return result;
    }

    private void saveAccount(String email, String password) {
        SharedPreferences.Editor editor = storage.edit();

        editor.clear();
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.commit();
    }
}

