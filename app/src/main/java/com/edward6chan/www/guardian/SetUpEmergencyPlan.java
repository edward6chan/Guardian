package com.edward6chan.www.guardian;

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

import android.view.Menu;
import android.view.View;

import com.getpebble.android.kit.Constants;
import com.getpebble.android.kit.PebbleKit;



public class SetUpEmergencyPlan extends Activity {


    final int PICK_CONTACT = 1;


    String phoneNumber;

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
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                        Intent i = new Intent(this, ConfirmAngel.class);
                        Bundle guardian_info = new Bundle();
                        guardian_info.putString("guardian_name", name);
                        guardian_info.putString("guardian_phone_number", phoneNumber);
                        i.putExtras(guardian_info);

                        SetUpEmergencyPlan.this.startActivity(i);

                    }
                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_emergency_plan);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.set_up_emergency_plan, menu);
        return true;
    }


    public void pickContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }

}
