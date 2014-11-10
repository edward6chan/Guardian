package com.edward6chan.www.guardian;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Krista on 11/4/14.
 */
public class MyCountdownTimer extends CountDownTimer {

    long total_seconds;
    int total_mins;
    int total_hours;
    int remaining_mins;
    int remaining_secs;
    TextView timer_set;
    String hoursToDisplay;
    String minsToDisplay;
    String secsToDisplay;
    ManageGuardian mParentManageGuardian;
    int mTimerType;

    public MyCountdownTimer(long millisInFuture, long countDownInterval, TextView timer, ManageGuardian parent, int timertype) {
        super(millisInFuture, countDownInterval);
        //Test:  Seconds - 600, Millis- 600,000
        //Test: 2 hours, 30 mins.
        // Seconds: 2*60*60 + 30*60 = 9000, Millis: 9,000,000

        //Example, 2 hours, 30 mins, 30 seconds
        //Milliseconds: 9030000, Seconds: 9030, Mins: 150, Hours: 2
        //Remaining mins: 30 , Remaining secs: 30 seconds
        timer_set=timer;
        mParentManageGuardian = parent;
        mTimerType=timertype;

        timerReset(millisInFuture, mTimerType);

//        total_seconds = millisInFuture / 1000;
//        total_mins = (int) total_seconds / 60;
//        total_hours = total_mins / 60;
//
//        remaining_mins = total_mins - total_hours * 60;
//
//        remaining_secs = (int) total_seconds - total_mins * 60;
//
//        hoursToDisplay = total_hours + "";
//        minsToDisplay = remaining_mins + "";
//        secsToDisplay = remaining_secs + "";
//
//        //not possible to put more than 9 hours on the timer.
//        /*if (total_hours >= 0 && total_hours <= 9) {
//            hoursToDisplay = "0" + total_hours;
//        }*/
//        if (remaining_mins >= 0 && remaining_mins <= 9) {
//            minsToDisplay = "0" + remaining_mins;
//        }
//        if (remaining_secs >= 0 && remaining_secs <= 9) {
//            secsToDisplay = "0" + remaining_secs;
//        }
//
//        timer_set.setText(hoursToDisplay + ":" + minsToDisplay + ":" + secsToDisplay);
    }

    public void timerReset(long millisInFuture, int type){

        total_seconds = millisInFuture / 1000;
        mTimerType = type;
        if (mTimerType == 0) {
            total_mins = (int) total_seconds / 60;
            total_hours = total_mins / 60;

            remaining_mins = total_mins - total_hours * 60;

            remaining_secs = (int) total_seconds - total_mins * 60;

            hoursToDisplay = total_hours + "";
            minsToDisplay = remaining_mins + "";
            secsToDisplay = remaining_secs + "";

            //not possible to put more than 9 hours on the timer.
        /*if (total_hours >= 0 && total_hours <= 9) {
            hoursToDisplay = "0" + total_hours;
        }*/
            if (remaining_mins >= 0 && remaining_mins <= 9) {
                minsToDisplay = "0" + remaining_mins;
            }
            if (remaining_secs >= 0 && remaining_secs <= 9) {
                secsToDisplay = "0" + remaining_secs;
            }

            timer_set.setText(hoursToDisplay + ":" + minsToDisplay + ":" + secsToDisplay);
        }
        if (mTimerType == 1){
            remaining_secs = (int)total_seconds;
            timer_set.setText(" " +secsToDisplay);
            this.start();
        }
    }
    Timer app1 = new Timer();

    @Override
    public void onFinish() {
        // TODO Auto-generated method stub
        this.cancel();
        if (mTimerType == 0) {
            timer_set.setText("0:00:00");
            mParentManageGuardian.timerDoneAskOk();
        }
        if (mTimerType == 1){
          timer_set.setText("Your angel will be contacted.");
          mParentManageGuardian.myDialog.dismiss();
          mParentManageGuardian.contactAngel();

        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // TODO Auto-generated method stub
        if (mTimerType == 0) {
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

        //not possible to put more than 9 hours on the timer.
        /*if (total_hours >= 0 && total_hours <= 9) {
            hoursToDisplay = "0" + total_hours;
        }*/
        if (remaining_mins >= 0 && remaining_mins <= 9) {
            minsToDisplay = "0" + remaining_mins;
        }
        if (remaining_secs >= 0 && remaining_secs <= 9) {
                secsToDisplay = "0" + remaining_secs;
            }

        timer_set.setText(hoursToDisplay + ":" + minsToDisplay + ":" + secsToDisplay);
        }
        if (mTimerType == 1){
            String secsToDisplay = "";


            remaining_secs--;

            secsToDisplay = remaining_secs + "";

            if (remaining_secs >= 0 && remaining_secs <= 9) {
                secsToDisplay = "0" + remaining_secs;
            }

            timer_set.setText(""+secsToDisplay);

        }
    }

}
