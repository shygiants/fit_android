package kr.ac.korea.ee.fit.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import kr.ac.korea.ee.fit.Authenticator;
import kr.ac.korea.ee.fit.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Authenticator authenticator = Authenticator.get(this);
        Bundle is_login = authenticator.isLogin();

        if (is_login.getString(Authenticator.KEY_ERROR_MESSAGE) != null)
            finish(); // TODO: Dialog
        if (is_login.getBoolean(Authenticator.IS_LOGIN)) {
            authenticator.onLogin();
//            Intent feed = new Intent(this, FeedActivity.class);
            Intent feed = new Intent(this, TabActivity.class);
            startActivity(feed);
            finish();
            return;
        }
        else if (is_login.getBoolean(Authenticator.HAS_ACCOUNT)) {
            Bundle signIn = authenticator.signIn();
            if (signIn.getString(Authenticator.KEY_ERROR_MESSAGE) != null)
                finish(); // TODO: Dialog
            if (signIn.getBoolean(Authenticator.IS_LOGIN)) {
                authenticator.onLogin();
//                Intent feed = new Intent(this, FeedActivity.class);
                Intent feed = new Intent(this, TabActivity.class);
                startActivity(feed);
                finish();
                return;
            }
        }

        // We need to get input from user
        Intent auth = new Intent(this, SignInActivity.class);
        startActivity(auth);
        finish();
    }
}
