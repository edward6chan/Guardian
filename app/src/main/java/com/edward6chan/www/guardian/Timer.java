package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.os.CountDownTimer;



public class Timer extends Activity {

    public TextView textView1;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERENCES", MODE_PRIVATE);
        String seconds = mSharedPreferences.getString("TIMER", null);
        int secondsInt = Integer.parseInt(seconds);

        secondsInt = secondsInt *1000 *60;

        textView1=(TextView) findViewById(R.id.textView1);

        MyCountDownTimer counter = new MyCountDownTimer(secondsInt,1000);
        counter.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timer, menu);
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
    public class MyCountDownTimer extends CountDownTimer{

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        Timer app1 = new Timer();

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub

            textView1.setText("done");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

            textView1.setText(Long.toString(millisUntilFinished / 1000));

        }
    }

}

