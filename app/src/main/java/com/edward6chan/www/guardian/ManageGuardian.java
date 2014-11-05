package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;


public class ManageGuardian extends FragmentActivity implements SensorEventListener, HmsPickerDialogFragment.HmsPickerDialogHandler {

    private SharedPreferences mSharedPreferences;

    private TextView textView;
    private TextView mToggleSwitch;

    String name, phoneNumber;


    //Sensor stuff
    private SensorManager mSensorManager;
    private Sensor mStepSensor;
    //private SensorEventListener mSensorEventListener = null;
    private TextView mTextView, mTimer_Set;
    private int mStep, seconds;
    private boolean isMoving = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_guardian);
        textView = (TextView) findViewById(R.id.stepCount);

        //getting shared preferences file
        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERENCES", MODE_PRIVATE);

        //getting angel contact name from shared preferences file
        String name = mSharedPreferences.getString("ANGEL_NAME", null);

        //getting angel contact number from shared preferences file
        String phoneNumber = mSharedPreferences.getString("guardian_phone_number", null);

        //getting saved time from shared preferences file
        String seconds = mSharedPreferences.getString("TIMER", null);

        //Bundle extras = getIntent().getExtras();
        //if (extras != null) {

            //pulling name from shared preferences
            TextView tv = (TextView)findViewById(R.id.angel_name);
            tv.setText(name);

            //pulling number from shared preferences
            tv = (TextView)findViewById(R.id.angel_phone_number);
            tv.setText(phoneNumber);

            //creating timer and displaying timer to correct textview
            mTimer_Set = (TextView) findViewById(R.id.timer_set);
            int secondsInt = Integer.parseInt(seconds);
            secondsInt = secondsInt * 1000;
            MyCountdownTimer counter = new MyCountdownTimer(secondsInt, 1000, mTimer_Set);
        //}

        mTextView = (TextView) findViewById(R.id.stepCount);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);



    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mStepSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mStepSensor);
    }

    protected void onStop() {
        super.onPause();
        mSensorManager.unregisterListener(this, mStepSensor);
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


    public void handleTimePicker(View v) {
        HmsPickerBuilder hmsPickerBuilder = new HmsPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment);
        hmsPickerBuilder.show();

    }
    @Override
    public void onDialogHmsSet(int i, int hour, int minute, int second) {
       
        seconds = hour*60*60 + minute*60 + second;
        int milliSeconds = seconds*1000;
        mSharedPreferences.edit().putString("TIMER", seconds + "").commit();
        MyCountdownTimer counter = new MyCountdownTimer(milliSeconds, 1000, mTimer_Set);

    }

    public void onSwitchClick(View v){

        Button button = (Button) v;
        Boolean isActive = false;

        mToggleSwitch = (TextView) findViewById(R.id.toggle_active_inactive);
        String active_inactive = mToggleSwitch.getText().toString();

        if (active_inactive == "ACTIVE") {
            mToggleSwitch.setText("INACTIVE");
            isActive = false;

        }
        else {
            mToggleSwitch.setText("ACTIVE");
            isActive = true;
        }
    }
    final int PICK_CONTACT = 1;

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    String[] projection = new String[]{
                            ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
                    Uri contacts = data.getData();
                    Cursor cursor = getContentResolver().query(contacts,
                            projection, // Which columns to return
                            null,       // Which rows to return (all rows)
                            // Selection arguments (with a given ID)
                            null,
                            // Put the results in ascending order by name
                            null);
                    if (cursor.moveToFirst()) {
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Intent i = new Intent(this, ManageGuardian.class);

                        //Save to shared preferences
                        mSharedPreferences.edit().putString("ANGEL_NAME", name + "").commit();
                        mSharedPreferences.edit().putString("guardian_phone_number", phoneNumber + "").commit();
                        /*Bundle guardian_info = new Bundle();
                        guardian_info.putString("guardian_name", name);
                        guardian_info.putString("guardian_phone_number", phoneNumber);
                        i.putExtras(guardian_info);*/
                        //i.putExtra("guardian_name", name);

                        ManageGuardian.this.startActivity(i);
                    }
                }
                break;
        }
    }
    public void onEditAngelClick(View v){
        ImageButton button = (ImageButton) v;
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (event.values[0] == 1.0f) {
            mStep++;
            isMoving = true;
        }
        else if (event.values[0] != 0.0f){
            isMoving = false;
        }

        mTextView.setText(Integer.toString(mStep));
        //mTextView.setText(Boolean.toString(isMoving));
        }
    /*
    public int getSteps(){
        return mStep;
    }

    public boolean isMoving(){
        boolean isMoving = false;

        if (event.values[0] == 1.0f) {
            isMoving = true;
        }
        else if

    return isMoving;
    }*/


}
