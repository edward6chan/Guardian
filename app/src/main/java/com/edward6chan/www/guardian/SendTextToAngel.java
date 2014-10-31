package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SendTextToAngel extends Activity {
    String name, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_text_to_angel);

        Bundle intentExtras = getIntent().getExtras();

        name = intentExtras.getString("guardian_name");
        phoneNumber = intentExtras.getString("guardian_phone_number");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_text_to_angel, menu);
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

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void sendTextMessageButton (View v) {

        Button button = (Button) v;

        sendSMS(phoneNumber, "Hi You got a message!");

        Intent i = new Intent(SendTextToAngel.this, ImmobileTimerScreenOne.class);
        SendTextToAngel.this.startActivity(i);

        //Intent i = new Intent(SendTextToAngel.this, ManageGuardian.class);
        //SendTextToAngel.this.startActivity(i);
    }
}
