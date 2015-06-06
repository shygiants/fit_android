package kr.ac.korea.ee.fit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import kr.ac.korea.ee.fit.R;
import kr.ac.korea.ee.fit.fragment.UserFragment;

/**
 * Created by SHY_mini on 15. 6. 6..
 */
public class CollectionActivity extends Activity {

    EditText nameText;
    EditText descriptionText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collection);

        nameText = (EditText)findViewById(R.id.nameText);
        descriptionText = (EditText)findViewById(R.id.descriptionText);
    }

    public void onClick(View view) {
        Intent result = new Intent();
        result.putExtra(UserFragment.NAME, nameText.getText().toString());
        result.putExtra(UserFragment.DESC, descriptionText.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }
}
