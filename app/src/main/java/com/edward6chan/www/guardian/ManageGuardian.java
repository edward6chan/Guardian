package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ManageGuardian extends Activity {

    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_guardian);

        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERENCES", MODE_PRIVATE);
        String name = mSharedPreferences.getString("ANGEL_NAME", null);

        String phoneNumber = mSharedPreferences.getString("guardian_phone_number", null);


        //Bundle extras = getIntent().getExtras();
        //if (extras != null) {

            //pulling name from shared preferences
            TextView tv = (TextView)findViewById(R.id.guardian_name);
            tv.setText(name);

            //pulling number from shared preferences
            tv = (TextView)findViewById(R.id.guardian_phone_number);
            tv.setText(phoneNumber);
        //}

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
}
