package com.edward6chan.www.guardian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    private SharedPreferences mSharedPreferences;
    private String mAngelName;
    private String mTimer;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;

        mSharedPreferences = getSharedPreferences("GUARDIAN_PREFERENCES", MODE_PRIVATE);
        mAngelName = mSharedPreferences.getString("ANGEL_NAME", null);
        mTimer = mSharedPreferences.getString("TIMER", null);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAngelName == null || mTimer == null) {
                    Intent welcomeIntent = new Intent(mContext, WelcomeScreen.class);
                    startActivity(welcomeIntent);
                    finish();
                } else {
                    Intent manageIntent = new Intent(mContext, ManageGuardian.class);
                    startActivity(manageIntent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
