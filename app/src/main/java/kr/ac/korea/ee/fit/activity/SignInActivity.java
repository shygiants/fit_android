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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);
    }

    public void onClickView(View view) {
        switch(view.getId()) {
            case R.id.signInButton:
                String email = ((EditText) findViewById(R.id.emailText)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
                Bundle signIn = Authenticator.get(this).signIn(email, password);
                if (signIn.getString(Authenticator.KEY_ERROR_MESSAGE) != null) {
                    finish();
                    return;
                }

                if (signIn.getBoolean(Authenticator.IS_LOGIN)) {
//                    Intent feed = new Intent(this, FeedActivity.class);
                    Intent feed = new Intent(this, TabActivity.class);
                    startActivity(feed);
                    finish();
                } else {
                    Toast.makeText(this, "아이디나 비밀번호가 올바르지 않습니다", Toast.LENGTH_LONG).show();
                }

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
            if (data.getBooleanExtra(Authenticator.IS_LOGIN, false)) {
//                Intent feed = new Intent(this, FeedActivity.class);
                Intent feed = new Intent(this, TabActivity.class);
                startActivity(feed);
                finish();
            }
            else if (data.hasExtra(Authenticator.KEY_ERROR_MESSAGE)) {
                finish();
            }

        }
    }
}
