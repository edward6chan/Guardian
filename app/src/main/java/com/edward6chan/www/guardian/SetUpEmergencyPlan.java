package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getpebble.android.kit.Constants;
import com.getpebble.android.kit.PebbleKit;

import java.util.List;


public class SetUpEmergencyPlan extends Activity {

    private final String TAG = "ExampleSmsActivity";

    final int PICK_CONTACT = 1;

    private LocationManager mLocationManager;
    private String mProviderName;
    private Handler mHandler;

    private boolean mLocationPending;

    String phoneNumber;

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    /*
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        Intent i = new Intent(getApplicationContext(), ManageGuardian.class);
                        i.putExtra("guardian_name", name);
                        startActivity(i);
                    }
                    */
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
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Intent i = new Intent(this, ConfirmAngel.class);
                        Bundle guardian_info = new Bundle();
                        guardian_info.putString("guardian_name", name);
                        guardian_info.putString("guardian_phone_number", phoneNumber);
                        i.putExtras(guardian_info);
                        //i.putExtra("guardian_name", name);

                        SetUpEmergencyPlan.this.startActivity(i);

                        //i.putExtra("guardian_phone_number", phoneNumber);
                        //requestLocationForSms();
                        //sendSMS(phoneNumber, "Hi You got a message!");
                        //startActivity(i);

                        // i = new Intent(SetUpEmergencyPlan.this, ImmobileTimerScreenOne.class);
                    }
                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_emergency_plan);
        PebbleKit.startAppOnPebble(getApplicationContext(), Constants.SPORTS_UUID);

        mLocationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);

        mProviderName = mLocationManager.getBestProvider(criteria, true);

        mHandler = new Handler();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.set_up_emergency_plan, menu);
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
    public void assignAngelButton (View v) {

        Button button = (Button) v;
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);

    }
    */

    public void pickContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            sendLocationSms(location);
            mLocationManager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle bundle) {
            Log.e(TAG, "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled");
        }
    };

    public void requestLocationForSms() {
        if (mProviderName != null && mLocationPending == false) {
            mLocationPending = true;

            Location lastLocation = mLocationManager.getLastKnownLocation(mProviderName);
            // if we have a location that's newer than 10 minutes, use it; otherwise get a new location
            if (lastLocation != null && (System.currentTimeMillis() - lastLocation.getTime() > DateUtils.MINUTE_IN_MILLIS * 10)) {
                mLocationManager.requestLocationUpdates(mProviderName,
                        10000,
                        10,
                        mLocationListener);
            } else {
                sendLocationSms(lastLocation);
            }
        }
    }

    public void sendLocationSms(Location l) {
        if (mLocationPending) {
            mLocationPending = false;

            // send SMS with GPS coordinates
            SmsManager smsManager = SmsManager.getDefault();
            String locationString = "Get me: " + l.getLatitude() + ", " + l.getLongitude();
            smsManager.sendTextMessage(phoneNumber, null, locationString, null, null);

            // get address text if we can
            Geocoder geocoder = new Geocoder(SetUpEmergencyPlan.this);

            try {
                List<Address> addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);

                if (addresses.size() > 0) {
                    Address a = addresses.get(0);
                    String addressText = "";
                    for (int i = 0; i <= a.getMaxAddressLineIndex(); i++) {
                        addressText += a.getAddressLine(i) + " ";
                    }
                    smsManager.sendTextMessage(phoneNumber, null, addressText, null, null);
                }
            } catch (Exception e) {
                // unable to geocode
            }
        }
    }
}
