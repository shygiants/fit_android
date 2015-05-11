package kr.ac.korea.ee.fit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import kr.ac.korea.ee.fit.Authenticator;
import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.request.Credential;

/**
 * Created by SHY_mini on 15. 4. 29..
 */
public class SignInActivity extends Activity {

    String email;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);
    }

    public void onClickView(View view) {
        switch(view.getId()) {
            case R.id.signInButton:
                email = ((EditText) findViewById(R.id.emailText)).getText().toString();
                password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
                // TODO: Form validation
                Credential inputCredential = new Credential(email, password);

                // TODO: Progress
                LoginTask loginTask = new LoginTask();
                loginTask.start(inputCredential);

                break;
            case R.id.signUpButton:
                Intent signUp = new Intent(this, SignUpActivity.class);
                startActivityForResult(signUp, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra(SignUpActivity.SUCCESS, false)) {
                email = data.getStringExtra(SignUpActivity.EMAIL);
                password = data.getStringExtra(SignUpActivity.PASSWORD);
                onLogin();
            }
            else
                finish();
        }
    }

    void startTabActivity() {
        Intent tab = new Intent(this, TabActivity.class);
        startActivity(tab);
        finish();
    }

    void onLogin() {
        Authenticator.saveAccount(this, email, password);
        startTabActivity();
    }

    void onLoginFailed() {
        Toast.makeText(this, "이메일이나 비밀번호가 잘못 입력하셨습니다", Toast.LENGTH_LONG).show();
    }

    private class LoginTask extends HTTPClient<Credential> {
        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                if (response == null || response.has("error"))
                    throw new Exception();

                if (response.getBoolean("is_login"))
                    onLogin(); // input account is valid
                else
                    onLoginFailed(); // input account is invalid
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
