package kr.ac.korea.ee.fit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.fragment.UserFragment;

/**
 * Created by SHYBook_Air on 15. 6. 6..
 */
public class ProfileActivity extends Activity {

    EditText nickNameText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        nickNameText = (EditText)findViewById(R.id.nickNameText);
    }

    public void onClick(View view) {
        Intent result = new Intent();
        result.putExtra(UserFragment.NICKNAME, nickNameText.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }
}
