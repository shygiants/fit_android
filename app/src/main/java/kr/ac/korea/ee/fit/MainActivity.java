package kr.ac.korea.ee.fit;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickView(View v)
    {
        String response = null;
        RateEvent rateEvent = new RateEvent();

        switch (v.getId())
        {
            case R.id.rate:
                String rating = null;
                switch (((RadioGroup)findViewById(R.id.rating)).getCheckedRadioButtonId())
                {
                    case R.id.rate1:
                        rating = "1";
                        break;
                    case R.id.rate2:
                        rating = "2";
                        break;
                    case R.id.rate3:
                        rating = "3";
                        break;
                    case R.id.rate4:
                        rating = "4";
                        break;
                    case R.id.rate5:
                        rating = "5";
                        break;
                }
                rateEvent.execute(rating);
                try {
                    response = rateEvent.get(); // get eventId
                } catch (Exception e) {
                    Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }
}
