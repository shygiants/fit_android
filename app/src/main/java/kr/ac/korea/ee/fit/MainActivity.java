package kr.ac.korea.ee.fit;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import io.prediction.Event;
import io.prediction.EventClient;


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
        EventClient client = new EventClient("wERSmq7bExLqaTR9FyJKMJtEOTu0ikH74Sf4ovyLc8G3vWGNsSX2NCa29YVshWLu",
                "163.152.21.217");

        switch (v.getId())
        {
            case R.id.rate:
                // A user rates an item
                Event rateEvent = new Event()
                        .event("rate")
                        .entityType("user")
                        .entityId("0")
                        .targetEntityType("item")
                        .targetEntityId("0")
                        .property("rating", new Float("5"));
                try {
                    client.createEvent(rateEvent);
                } finally {
                    return;
                }


            case R.id.buy:
                // A user buys an item
                Event buyEvent = new Event()
                        .event("buy")
                        .entityType("user")
                        .entityId("0")
                .targetEntityType("item")
                    .targetEntityId("0");

                try {
                    client.createEvent(buyEvent);
                } finally {
                    return;
                }
        }
    }
}
