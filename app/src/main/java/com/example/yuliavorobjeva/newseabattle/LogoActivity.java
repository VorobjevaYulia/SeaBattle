package com.example.yuliavorobjeva.newseabattle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class LogoActivity extends AppCompatActivity {

    private ImageView mLogo;
    private TextView mAppName;
    private Typeface typeface;
    private Animation mAnimation;
    private SharedPreferences mUserData = null;
    private String mLoadedEmail = null;
    private String mLoadedPassword = null;
    //final String USER_EMAIL = "user_email";
    //final String USER_PASSWORD = "user_password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/main_font.ttf");

        mLogo = (ImageView) findViewById(R.id.logo_image);
        mAppName = (TextView) findViewById(R.id.app_name);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        mLogo.startAnimation(mAnimation);
        mAppName.setTypeface(typeface);

        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.logoStatusBar));

        MyTask myTask = new MyTask();
        myTask.execute();

    }

    class MyTask extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mUserData = getSharedPreferences("USER_DATA",MODE_PRIVATE);
            //mLoadedEmail = mUserData.getString(USER_EMAIL, "");
            //mLoadedPassword = mUserData.getString(USER_PASSWORD, "");
            Intent intent = new Intent(LogoActivity.this, RegistrationActivity.class );
            //if (mLoadedEmail.equals("")) {
                startActivity(intent);
            //} else {
                //intent.putExtra("email", mLoadedEmail);
                //intent.putExtra("pass", mLoadedPassword);
                //intent.putExtra("from", "LogoActivity");
              //  startActivity(intent);
            //}
        }
    }
}


