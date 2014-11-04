package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


public class ManageGuardian extends Activity implements SensorEventListener {

    private SharedPreferences mSharedPreferences;

    private TextView textView;
    private TextView mToggleSwitch;
    private TextView mTimer;

    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_guardian);
        textView = (TextView) findViewById(R.id.stepCount);



        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERENCES", MODE_PRIVATE);
        String name = mSharedPreferences.getString("ANGEL_NAME", null);

        String phoneNumber = mSharedPreferences.getString("guardian_phone_number", null);

        String timeSaved = mSharedPreferences.getString("TIMER",null);


        //Bundle extras = getIntent().getExtras();
        //if (extras != null) {

            //pulling name from shared preferences
            TextView tv = (TextView)findViewById(R.id.angel_name);
            tv.setText(name);

            //pulling number from shared preferences
            tv = (TextView)findViewById(R.id.angel_phone_number);
            tv.setText(phoneNumber);

            //pulling time saved from saved preferences (
            // !!need to fix to display correctly as 0:00:00
            mTimer = (TextView) findViewById(R.id.timer_set);
            if (timeSaved != null) {
                mTimer.setText(timeSaved);
            }

        //}


        mSensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manage_guardian, menu);
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

    protected void onResume() {

        super.onResume();

        mSensorManager.registerListener(this, mStepCounterSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,

                SensorManager.SENSOR_DELAY_FASTEST);

    }

    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    public void onAccuracyChanged(Sensor e, int accuracy) {}
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            textView.setText("Step Counter Detected : " + value);
        } /*else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
            textView.setText("Step Detector Detected : " + value);
        }*/
    }

    public void onSwitchClick(View v){

        Button button = (Button) v;
        Boolean active = false;

        mToggleSwitch = (TextView) findViewById(R.id.toggle_active_inactive);
        String active_inactive = mToggleSwitch.getText().toString();

        if (active_inactive == "ACTIVE") {
            mToggleSwitch.setText("INACTIVE");
            active = false;

        }
        else {
            mToggleSwitch.setText("ACTIVE");
            active = true;
        }
    }
}
