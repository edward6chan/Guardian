package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;


public class ConfirmAngel extends Activity {

    private SharedPreferences mSharedPreferences;
    String name, phoneNumber;

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

                        Intent i = new Intent(this, ConfirmAngel.class);
                        Bundle guardian_info = new Bundle();
                        guardian_info.putString("guardian_name", name);
                        guardian_info.putString("guardian_phone_number", phoneNumber);
                        i.putExtras(guardian_info);
                        //i.putExtra("guardian_name", name);

                        ConfirmAngel.this.startActivity(i);
                    }
                }
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_angel);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        Bundle intentExtras = getIntent().getExtras();
        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERENCES", MODE_PRIVATE);

        name = intentExtras.getString("guardian_name");
        phoneNumber = intentExtras.getString("guardian_phone_number");

        TextView tvContact = (TextView) findViewById(R.id.tv_angel_info);

        tvContact.setText(name + ", " + phoneNumber);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confirm_angel, menu);
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

    public void confirmAngelButton(View v) {

        Bundle guardian_info = new Bundle();


        mSharedPreferences.edit().putString("ANGEL_NAME", name + "").commit();
        mSharedPreferences.edit().putString("guardian_phone_number", phoneNumber + "").commit();

        Intent i = new Intent(ConfirmAngel.this, SendTextToAngel.class);

        guardian_info.putString("guardian_name", name);
        guardian_info.putString("guardian_phone_number", phoneNumber);
        i.putExtras(guardian_info);

        ConfirmAngel.this.startActivity(i);
    }

    public void cancelAngelButton(View v) {

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);


    }
}
