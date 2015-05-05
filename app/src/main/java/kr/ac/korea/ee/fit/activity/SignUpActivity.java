package kr.ac.korea.ee.fit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import kr.ac.korea.ee.fit.Authenticator;
import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.HTTPClient;
import kr.ac.korea.ee.fit.request.Credential;

/**
 * Created by SHYBook_Air on 15. 5. 5..
 */
public class SignUpActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
    }

    public void onClickView(View view) {
        String email = ((EditText) findViewById(R.id.emailText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        String rePassword = ((EditText) findViewById(R.id.rePasswordText)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.lastNameText)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.firstNameText)).getText().toString();

        Bundle signUp = Authenticator.get(this).signUp(email, password, rePassword, firstName, lastName);
        if (signUp.getString(Authenticator.KEY_ERROR_MESSAGE) != null) {
            Intent result = new Intent();
            result.putExtra(Authenticator.KEY_ERROR_MESSAGE, true);
            setResult(RESULT_OK, result);
            finish();
            return;
        }
        if (signUp.getBoolean(Authenticator.IS_LOGIN)) {
            Intent result = new Intent();
            result.putExtra(Authenticator.IS_LOGIN, true);
            setResult(RESULT_OK, result);
            finish();
        }

    }
}
