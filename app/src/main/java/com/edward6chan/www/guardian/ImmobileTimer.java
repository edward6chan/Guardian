package com.edward6chan.www.guardian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;


public class ImmobileTimer extends FragmentActivity implements HmsPickerDialogFragment.HmsPickerDialogHandler {

    private int seconds;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immobile_timer);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERENCES", MODE_PRIVATE);

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
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        return true;
    }


    @Override
    public void onDialogHmsSet(int i, int hour, int minute, int second) {
        //displayTimer.setText("0" + hour + ":" + minute + ":" + second);
        seconds = hour*60*60 + minute*60 + second;
        mSharedPreferences.edit().putString("TIMER", seconds + "").commit();
        Intent manageGuardian = new Intent(this, ManageGuardian.class);
        ImmobileTimer.this.startActivity(manageGuardian);
    }
}
