package com.edward6chan.www.guardian;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.content.Intent;
import android.view.View;


public class ImmobileTimerScreenTwo extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immobile_timer_screen_two);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.immobile_timer_screen_two, menu);
        return true;
    }



    public void nextButton (View v) {

        Intent intentWelcome = new Intent(ImmobileTimerScreenTwo.this, ImmobileTimer.class);
        ImmobileTimerScreenTwo.this.startActivity(intentWelcome);
    }
}
