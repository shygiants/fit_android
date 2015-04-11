package kr.ac.korea.ee.fit.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.client.Authenticator;
import kr.ac.korea.ee.fit.model.Credential;
import kr.ac.korea.ee.fit.model.Get;


public class AuthenticatorActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        CookieHandler.setDefault(new CookieManager());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_authenticator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.signInButton:
                String email = ((EditText) findViewById(R.id.emailText)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
                Credential credential = new Credential(email, password);

                Authenticator auth = new Authenticator();
                auth.start(credential);

                JSONObject authenticated = null;
                try {
                    authenticated = auth.get();
                } catch (Exception e) {
                    Log.e("AuthenticatorActivity", "auth.get Exception");
                }

                TextView response = (TextView) findViewById(R.id.responseText);
                try {
                    response.setText("Authenticated: " + ((authenticated != null) ? authenticated.getString("is_authenticated") : "null"));
                } catch (JSONException e) {
                    Log.e("AuthenticatorActivity", "JSONException");
                }
                break;
            case R.id.registerButton:
                String email_reg = ((EditText) findViewById(R.id.emailText)).getText().toString();
                String password_reg = ((EditText) findViewById(R.id.passwordText)).getText().toString();
                Credential credential_reg = new Credential(email_reg, password_reg, password_reg, "Sanghun", "Yun");

                Authenticator reg = new Authenticator();
                reg.start(credential_reg);

                JSONObject registered = null;
                try {
                    registered = reg.get();
                } catch (Exception e) {
                    Log.e("AuthenticatorActivity", "auth.get Exception");
                }

                TextView response_reg = (TextView)findViewById(R.id.responseText);
                try {
                    response_reg.setText("Registered: " + ((registered != null) ? registered.getString("is_registered") : "null"));
                } catch (JSONException e) {
                    Log.e("AuthenticatorActivity", "JSONException");
                }
                break;
            case R.id.loginTestButton:
                Get get = new Get();

                Authenticator get_auth = new Authenticator();
                get_auth.start(get);

                JSONObject is_login = null;
                try {
                    is_login = get_auth.get();
                } catch (Exception e) {
                    Log.e("AuthenticatorActivity", "auth.get Exception");
                }

                TextView is_login_text = (TextView)findViewById(R.id.responseText);
                try {
                    is_login_text.setText("Is Login: " + ((is_login != null) ? is_login.getString("is_login") : "null"));
                } catch (JSONException e) {
                    Log.e("AuthenticatorActivity", "JSONException");
                }
                break;
        }
    }
}
