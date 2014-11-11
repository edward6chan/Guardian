package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class SendTextToAngel extends Activity {
    String name, phoneNumber;
    private EditText text_message;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_text_to_angel);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

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


    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void sendTextMessageButton (View v) {

        text_message = (EditText) findViewById(R.id.TextMessage);
        text = text_message.getText().toString();

        sendSMS(phoneNumber, text);

        Intent i = new Intent(SendTextToAngel.this, ImmobileTimerScreenOne.class);
        SendTextToAngel.this.startActivity(i);

    }
}
