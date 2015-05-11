package kr.ac.korea.ee.fit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import kr.ac.korea.ee.fit.Authenticator;
import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.ResponseException;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.request.Credential;

/**
 * Created by SHYBook_Air on 15. 5. 5..
 */
public class SignUpActivity extends Activity {

    public static final String SUCCESS = "SUCCESS";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";

    String email;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
    }

    public void onClickView(View view) {
        email = ((EditText) findViewById(R.id.emailText)).getText().toString();
        password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        String rePassword = ((EditText) findViewById(R.id.rePasswordText)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.lastNameText)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.firstNameText)).getText().toString();

        // TODO: Form validation

        Credential inputCredential = new Credential(email, password, firstName, lastName);
        SignUpTask signUpTask = new SignUpTask();
        signUpTask.start(inputCredential);
    }

    void onEmailAlreadyExists() {
        Toast.makeText(this, "이메일이 이미 존재합니다", Toast.LENGTH_LONG).show();
    }

    void onLogin() {
        Intent result = new Intent();
        result.putExtra(SUCCESS, true);
        result.putExtra(EMAIL, email);
        result.putExtra(PASSWORD, password);
        setResult(RESULT_OK, result);
        finish();
    }

    void onError() {
        Intent result = new Intent();
        result.putExtra(SUCCESS, false);
        setResult(RESULT_OK, result);
        finish();
    }

    private class SignUpTask extends HTTPClient<Credential> {
        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                if (response == null)
                    throw new Exception();
                if (response.has("error")) {
                    if (response.getString("error").equals(ResponseException.EMAIL))
                        onEmailAlreadyExists(); // input email already exists
                    else throw new Exception();
                    return;
                }

                if (response.getBoolean("is_login"))
                    onLogin(); // input account is registered
            } catch (JSONException e) {
                // fatal error
                e.printStackTrace();
                Log.e("LoginTask", response.toString());
                onError();
            } catch (Exception e) {
                // fatal error
                e.printStackTrace();
                // TODO: Dialog
                onError();
            }
        }
    }
}
