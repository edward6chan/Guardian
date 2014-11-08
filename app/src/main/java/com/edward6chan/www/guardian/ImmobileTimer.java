package com.edward6chan.www.guardian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;


public class ImmobileTimer extends FragmentActivity implements HmsPickerDialogFragment.HmsPickerDialogHandler {

    private int seconds;
    private TextView displayTimer;
    private Button bTimePicker;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immobile_timer);

        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERENCES", MODE_PRIVATE);

        displayTimer = (TextView) findViewById(R.id.test_timer);
        bTimePicker = (Button) findViewById(R.id.button_timer_picker);

        String timeSaved = mSharedPreferences.getString("TIMER", null);
        if (timeSaved != null) {
            displayTimer.setText(timeSaved);
        }

    }

    public void handleTimePicker(View v) {
        HmsPickerBuilder hmsPickerBuilder = new HmsPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment);
        hmsPickerBuilder.show();

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

    public void nextButton(View v) {

        mSharedPreferences.edit().putString("TIMER", seconds + "").commit();
        Intent i = new Intent(ImmobileTimer.this, Timer.class);
        ImmobileTimer.this.startActivity(i);
    }


    @Override
    public void onDialogHmsSet(int i, int hour, int minute, int second) {
        displayTimer.setText("0" + hour + ":" + minute + ":" + second);
        seconds = hour*60*60 + minute*60 + second;
    }
}
