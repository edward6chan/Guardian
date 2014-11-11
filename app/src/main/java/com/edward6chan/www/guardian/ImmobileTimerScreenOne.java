package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class ImmobileTimerScreenOne extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immobile_timer_screen_one);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.immobile_timer_screen_one, menu);
        return true;
    }

    public void nextButton (View v) {

        Intent intentWelcome = new Intent(ImmobileTimerScreenOne.this, ImmobileTimerScreenTwo.class);
        ImmobileTimerScreenOne.this.startActivity(intentWelcome);
    }
}
