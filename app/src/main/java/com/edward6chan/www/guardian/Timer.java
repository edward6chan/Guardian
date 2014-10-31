package com.edward6chan.www.guardian;

/**
 * Created by Krista on 10/31/14.
 */

public class Timer {
        private long end;
        private final long start;
        private final long period;

        /**
         * Instantiates a new Timer with a given time
         * period in milliseconds.
         *
         * @param period Time period in milliseconds.
         */
        public Timer(final long period) {
            this.period = period;
            start = System.currentTimeMillis();
            end = start + period;
        }

        /**
         * Returns the number of milliseconds elapsed since
         * the start time.
         *
         * @return The elapsed time in milliseconds.
         */
    public long getElapsed() {
        return System.currentTimeMillis() - start;
    }

    /**
     * Returns the number of milliseconds remaining
     * until the timer is up.
     *
     * @return The remaining time in milliseconds.
     */
    public long getRemaining() {
        if (isRunning()) {
            return end - System.currentTimeMillis();
        }
        return 0;
    }

    /**
     * Returns <tt>true</tt> if this timer's time period
     * has not yet elapsed.
     *
     * @return <tt>true</tt> if the time period has not yet passed.
     */
    public boolean isRunning() {
        return System.currentTimeMillis() < end;
    }

    /**
     * Restarts this timer using its period.
     */
    public void reset() {
        end = System.currentTimeMillis() + period;
    }

    /**
     * Sets the end time of this timer to a given number of
     * milliseconds from the time it is called. This does
     * not edit the period of the timer (so will not affect
     * operation after reset).
     *
     * @param ms The number of milliseconds before the timer
     *           should stop running.
     * @return The new end time.
     */
    public long setEndIn(final long ms) {
        end = System.currentTimeMillis() + ms;
        return end;
    }

    /**
     * Returns a formatted String of the time elapsed.
     *
     * @return The elapsed time formatted hh:mm:ss.

    public String toElapsedString() {
        return Time.format(getElapsed());
    }


     * Returns a formatted String of the time remaining.
     *
     * @return The remaining time formatted hh:mm:ss.

    public String toRemainingString() {
        return Time.format(getRemaining());
    }
     */
}





/*

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity {

    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        timerTextView = (TextView) findViewById(R.id.timerTextView);

        Button b = (Button) findViewById(R.id.button);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }
            }
        });
    }

  @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.button);
        b.setText("start");
    }

}







 */

