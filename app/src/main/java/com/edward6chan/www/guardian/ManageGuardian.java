package com.edward6chan.www.guardian;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionClient;

import java.util.UUID;


public class ManageGuardian extends FragmentActivity implements SensorEventListener, HmsPickerDialogFragment.HmsPickerDialogHandler, GooglePlayServicesClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private SharedPreferences mSharedPreferences;
    private final String TAG = "ManageGuardian";

    private TextView textView;
    private TextView mToggleSwitch;

    String name, phoneNumber;

    // Constants that define the activity detection interval
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int DETECTION_INTERVAL_SECONDS = 1;
    public static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    public enum REQUEST_TYPE {START, STOP}

    private REQUEST_TYPE mRequestType;

    private BroadcastReceiver mActivityBroadcastReceiver;
    private IntentFilter filter;

    String activityPerformed;
    int confidence;

    MyCountdownTimer mImmobileTimer;

    //private static final int TEMP_KEY = 1;                      // for Pebble Watch testing
    //private static final UUID GUARDIAN_UUID = UUID.fromString("playground.c");


    /*
     * Store the PendingIntent used to send activity recognition events
     * back to the app
     */
    private PendingIntent mActivityRecognitionPendingIntent;
    // Store the current activity recognition client
    private ActivityRecognitionClient mActivityRecognitionClient;

    //Sensor stuff
    private SensorManager mSensorManager;
    private Sensor mStepSensor;
    //private SensorEventListener mSensorEventListener = null;
    private TextView mTextView, mTimer_Set;
    private int mStep, seconds;
    private boolean isMoving = false;
    private Context mContext;

    // Flag that indicates if a request is underway.
    private boolean mInProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_guardian);

        mContext = this;

        mActivityRecognitionClient = new ActivityRecognitionClient(mContext, this, this);

        /*
         * Create the PendingIntent that Location Services uses
         * to send activity recognition updates back to this app.
         */
        Intent intent = new Intent(mContext, ActivityRecognitionIntentService.class);

        /*
         * Return a PendingIntent that starts the IntentService.
         */
        mActivityRecognitionPendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Start with the request flag set to false
        mInProgress = false;

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
        TextView tv = (TextView) findViewById(R.id.angel_name);
        tv.setText(name);

        //pulling number from shared preferences
        tv = (TextView) findViewById(R.id.angel_phone_number);
        tv.setText(phoneNumber);

        //creating timer and displaying timer to correct textview
        mTimer_Set = (TextView) findViewById(R.id.timer_set);
        int secondsInt = Integer.parseInt(seconds);
        secondsInt = secondsInt * 1000;
        mImmobileTimer = new MyCountdownTimer(secondsInt, 1000, mTimer_Set);


        //}

        mTextView = (TextView) findViewById(R.id.stepCount);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //sendGuardianToWatch(name);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume() hit.");
        super.onResume();
        mSensorManager.registerListener(this, mStepSensor, SensorManager.SENSOR_DELAY_FASTEST);

        //move to be toggled when active
        //startUpdates();
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
    protected void onDestroy() {
        super.onDestroy();
        stopUpdates();
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

    /*
     * LOCATION SERVICES METHODS
     */

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    //timer start

    public void startImmobileTimer(){
        mImmobileTimer.start();
    }

    /**
     * Request activity recognition updates based on the current
     * detection interval.
     */
    public void startUpdates() {
        Log.i(TAG, "startUpdates() hit.");


        // Set the request type to START
        mRequestType = REQUEST_TYPE.START;
        /*
         * Test for Google Play services after setting the request type.
         * If Google Play services isn't present, the proper request type
         * can be restarted.
         */

        // Check for Google Play services
        if (!servicesConnected()) {
            return;
        }

        // If a request is not already underway
        if (!mInProgress) {
            Log.i(TAG, "Not in progress");
            // Indicate that a request is in progress
            mInProgress = true;
            // Request a connection to Location Services
            mActivityRecognitionClient.connect();


            //
        } else {
            /*
             * A request is already underway. You can handle
             * this situation by disconnecting the client,
             * re-setting the flag, and then re-trying the
             * request.
             */
        }
        //Register broadcast receiver here, to start listening
        mActivityBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //called every time it receives something
                //'intent' stores information that the intent that is sending the info stores for this receiver
                // in your case --- ActivityRecognitionIntentService
                // intent.getStringExtra("Activity") --- stores the activity name/code
                // intent.getExtras().getInt("Confidence") -- corresponding confidence (100% etc..)

                activityPerformed = intent.getStringExtra("Activity");
                confidence = intent.getExtras().getInt("Confidence");

                Log.i(TAG, "Activity: " + activityPerformed +", " + "Confidence: " + confidence);

                textView.setText(activityPerformed+ ", " + confidence);
                //mImmobileTimer.onTick(long );

                String still = "still";
                if (activityPerformed == still){
                //    mImmobileTimer.start();
                    Log.i(TAG, "it is still timer should have started");
                }
                else{
                    mImmobileTimer.cancel();

                }
            }




        };


        filter = new IntentFilter();

        filter.addAction("com.edward6chan.www.guardian.ACTIVITY_RECOGNITION_DATA");

        registerReceiver(mActivityBroadcastReceiver, filter);
    }

    /**
     * Turn off activity recognition updates
     */
    public void stopUpdates() {
        Log.i(TAG, "stopUpdates() hit.");

        // Set the request type to STOP
        mRequestType = REQUEST_TYPE.STOP;
        /*
         * Test for Google Play services after setting the request type.
         * If Google Play services isn't present, the request can be
         * restarted.
         */
        if (!servicesConnected()) {
            return;
        }
        // If a request is not already underway
        if (!mInProgress) {
            // Indicate that a request is in progress
            mInProgress = true;
            // Request a connection to Location Services
            mActivityRecognitionClient.connect();
            //
        } else {
            /*
             * A request is already underway. You can handle
             * this situation by disconnecting the client,
             * re-setting the flag, and then re-trying the
             * request.
             */
        }
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        Log.i(TAG, "onConnected() hit.");

        switch (mRequestType) {
            case START:
                Log.i(TAG, "Case: START");

                /*
                 * Request activity recognition updates using the
                 * preset detection interval and PendingIntent.
                 * This call is synchronous.
                 */
                mActivityRecognitionClient.requestActivityUpdates(DETECTION_INTERVAL_MILLISECONDS, mActivityRecognitionPendingIntent);

                break;

            case STOP:
                Log.i(TAG, "Case: STOP");

                mActivityRecognitionClient.removeActivityUpdates(mActivityRecognitionPendingIntent);
                break;
                /*
                 * An enum was added to the definition of REQUEST_TYPE,
                 * but it doesn't match a known case. Throw an exception.
                 */
            default:
                try {
                    throw new Exception("Unknown request type in onConnected().");
                } catch (Exception e) {
                    Log.e(TAG, "Exception thrown: " + e.getMessage());
                }
                break;
        }
    }


    @Override
    public void onDisconnected() {
        Log.i(TAG, "onDisconnected() hit.");

        // Turn off the request flag
        mInProgress = false;
        // Delete the client
        mActivityRecognitionClient = null;
    }

    // Implementation of OnConnectionFailedListener.onConnectionFailed
    //for google play services
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed() hit.");

        // Turn off the request flag
        mInProgress = false;
        /*
         * If the error has a resolution, start a Google Play services
         * activity to resolve it.
         */
        if (connectionResult.hasResolution()) {
            try {

                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
            // If no resolution is available, display an error dialog
        } else {
            // Get the error code
            int errorCode = connectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(), "Activity Recognition");
            }
        }
    }

    public void handleTimePicker(View v) {
        HmsPickerBuilder hmsPickerBuilder = new HmsPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment);
        hmsPickerBuilder.show();

    }




    @Override
    public void onDialogHmsSet(int i, int hour, int minute, int second) {

        seconds = hour * 60 * 60 + minute * 60 + second;
        int milliSeconds = seconds * 1000;
        mSharedPreferences.edit().putString("TIMER", seconds + "").commit();
        MyCountdownTimer counter = new MyCountdownTimer(milliSeconds, 1000, mTimer_Set);

    }

    public void onSwitchClick(View v) {

        Button button = (Button) v;
        Boolean isActive = false;

        mToggleSwitch = (TextView) findViewById(R.id.toggle_active_inactive);
        String active_inactive = mToggleSwitch.getText().toString();

        if (active_inactive == "ACTIVE") {
            mToggleSwitch.setText("INACTIVE");
            isActive = false;
            stopUpdates();

           // stop broadcast receiver
            unregisterReceiver(mActivityBroadcastReceiver);

        } else {
            mToggleSwitch.setText("ACTIVE");
            isActive = true;
            startUpdates();
            startImmobileTimer();
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

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                    /*
                     * TODO: TRY REQUEST AGAIN
                     */
                        break;
                }
        }
    }

    //for google play services
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Activity Recognition", "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(), "Activity Recognition");
            }
            return false;
        }
    }

    public void onEditAngelClick(View v) {
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
        } else if (event.values[0] != 0.0f) {
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
/*
    public void sendGuardianToWatch(String guardian) {
        PebbleDictionary data = new PebbleDictionary();
        data.addString(TEMP_KEY, guardian);

        PebbleKit.sendDataToPebble(getApplicationContext(), GUARDIAN_UUID, data);
    }*/
}
