package kr.ac.korea.ee.fit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import kr.ac.korea.ee.fit.Authenticator;
import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.request.Credential;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Credential savedAccount = Authenticator.getSavedAccount(this);
        if (savedAccount != null) {
            // there is saved account
            LoginTask loginTask = new LoginTask();
            loginTask.start(savedAccount);
        }
        else
            startSignInActivity(); // there is no saved account
    }

    void startTabActivity() {
        Intent tab = new Intent(this, TabActivity.class);
        startActivity(tab);
        finish();
    }

    void startSignInActivity() {
        Intent signIn = new Intent(this, SignInActivity.class);
        startActivity(signIn);
        finish();
    }

    void onLogin() {
        Authenticator.onLogin(this);
        startTabActivity();
    }

    void onLoginFailed() {
        Authenticator.deleteAccount(this);
        startSignInActivity();
    }

    private class LoginTask extends HTTPClient<Credential> {

        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                if (response == null || response.has("error"))
                    throw new Exception();

                if (response.getBoolean("is_login"))
                    onLogin(); // saved account is valid
                else
                    onLoginFailed(); // saved account is invalid
            } catch (JSONException e) {
                // fatal error
                e.printStackTrace();
                Log.e("LoginTask", response.toString());
                finish();
            } catch (Exception e) {
                // fatal error
                e.printStackTrace();
                // TODO: Dialog
                finish();
            }
        }
    }
}
