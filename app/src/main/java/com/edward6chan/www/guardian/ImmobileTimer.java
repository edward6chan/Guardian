package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ImmobileTimer extends Activity {

    private EditText setTimer;
    private int minutes;
    private TextView displayTimer;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immobile_timer);

        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERNCES", MODE_PRIVATE);

        String timeSaved = mSharedPreferences.getString("TIMER", null);
        if(timeSaved!=null) {
            displayTimer.setText(timeSaved);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.immobile_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void nextButton (View v) {

        Button button = (Button) v;
        setTimer = (EditText) findViewById(R.id.timer_input);
        displayTimer = (TextView) findViewById(R.id.test_timer);
        minutes = Integer.parseInt(setTimer.getText().toString());
        displayTimer.setText(""+ minutes);

        mSharedPreferences.edit().putString("TIMER", minutes+"").commit();
    }
}
