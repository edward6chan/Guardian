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

        secondsInt = secondsInt * 1000;

        textView1 = (TextView) findViewById(R.id.textView1);

        MyCountDownTimer counter = new MyCountDownTimer(secondsInt, 1000);

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

    public class MyCountDownTimer extends CountDownTimer {

        long total_seconds;
        int total_mins;
        int total_hours;
        int remaining_mins;
        int remaining_secs;


        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            //Test:  Seconds - 600, Millis- 600,000
            //Test: 2 hours, 30 mins.
            // Seconds: 2*60*60 + 30*60 = 9000, Millis: 9,000,000

            //Example, 2 hours, 30 mins, 30 seconds
            //Milliseconds: 9030000, Seconds: 9030, Mins: 150, Hours: 2
            //Remaining mins: 30 , Remaining secs: 30 seconds


            String hoursToDisplay = "";
            String minsToDisplay = "";
            String secsToDisplay = "";

            total_seconds = millisInFuture / 1000;
            total_mins = (int) total_seconds / 60;
            total_hours = total_mins / 60;

            remaining_mins = total_mins - total_hours * 60;

            remaining_secs = (int) total_seconds - total_mins * 60;

            if (total_hours >= 0 && total_hours <= 9) {
                hoursToDisplay = "0" + total_hours;
            }
            if (remaining_mins >= 0 && remaining_mins <= 9) {
                minsToDisplay = "0" + remaining_mins;
            }
            if (remaining_secs >= 0 && remaining_secs <= 9) {
                secsToDisplay = "0" + remaining_secs;
            }

            textView1.setText(hoursToDisplay + ":" + minsToDisplay + ":" + secsToDisplay);
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

            String hoursToDisplay = "";
            String minsToDisplay = "";
            String secsToDisplay = "";

            // 01:24:03
            if (remaining_secs == 0 && millisUntilFinished > 0) {
                remaining_secs = 59;
                if (remaining_mins == 0) {
                    remaining_mins = 59;
                    total_hours--;
                } else {
                    remaining_mins--;
                }

            } else {
                remaining_secs--;
            }

            hoursToDisplay = total_hours + "";
            minsToDisplay = remaining_mins + "";
            secsToDisplay = remaining_secs + "";

            if (total_hours >= 0 && total_hours <= 9) {
                hoursToDisplay = "0" + total_hours;
            }
            if (remaining_mins >= 0 && remaining_mins <= 9) {
                minsToDisplay = "0" + remaining_mins;
            }
            if (remaining_secs >= 0 && remaining_secs <= 9) {
                secsToDisplay = "0" + remaining_secs;
            }

            textView1.setText(hoursToDisplay + ":" + minsToDisplay + ":" + secsToDisplay);

        }
    }

}

