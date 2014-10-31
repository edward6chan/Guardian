package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;


public class ConfirmAngel extends Activity {

    private SharedPreferences mSharedPreferences;
    String name, phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_angel);

        Bundle intentExtras = getIntent().getExtras();
        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERENCES", MODE_PRIVATE);

        name = intentExtras.getString("guardian_name");
        phoneNumber = intentExtras.getString("guardian_phone_number");

        TextView tvContact = (TextView)findViewById(R.id.tv_angel_info);

        tvContact.setText(name +", " + phoneNumber);
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

    public void confirmAngelButton (View v) {

        Button button = (Button) v;
        Bundle guardian_info = new Bundle();


        mSharedPreferences.edit().putString("ANGEL_NAME", name+"").commit();
        mSharedPreferences.edit().putString("guardian_phone_number", phoneNumber+"").commit();

        Intent i = new Intent(ConfirmAngel.this, SendTextToAngel.class);

        guardian_info.putString("guardian_name", name);
        guardian_info.putString("guardian_phone_number", phoneNumber);
        i.putExtras(guardian_info);

        ConfirmAngel.this.startActivity(i);
    }
}
