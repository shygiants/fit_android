package kr.ac.korea.ee.fit;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        TextView responseText = (TextView)findViewById(R.id.responseText);
        String response = null;
        EventClient eventClient = new EventClient();

        switch (v.getId())
        {
            case R.id.rate:
                eventClient.execute("rate");
                try {
                    response = eventClient.get(); // get eventId
                } catch (Exception e) {
                    Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT);
                }
                responseText.setText(response);
                responseText.setTextSize(20);
                break;

            case R.id.buy:
                eventClient.execute("rate");
                try {
                    response = eventClient.get(); // get eventId
                } catch (Exception e) {
                    Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT);
                }
                responseText.setText(response);
                responseText.setTextSize(20);
                break;
        }
    }
}
