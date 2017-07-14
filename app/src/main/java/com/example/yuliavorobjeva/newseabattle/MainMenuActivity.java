package com.example.yuliavorobjeva.newseabattle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;

    //UI components
    private TextView mMainText;
    private Button mStartBtn;
    private Button mAccountBtn;
    private Button mReitingBtn;
    private Typeface mMainTypeFace;
    private Typeface mAllTypeFace;
    private ProgressDialog mProgressDialog;

    //Data for Firebase DataBase
    private DatabaseReference mDatabase;
    private DatabaseReference mCountOnlineUsers;
    private FirebaseAuth mAuth;

    private String myEmail;
    private String mEmailForFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent intent = getIntent();
//        myEmail = intent.getStringExtra("email");
  //      myEmail = myEmail.replace('.', '_');


        mContext = MainMenuActivity.this;

        mMainText = (TextView) findViewById(R.id.main_menu_main_text);
        mStartBtn = (Button) findViewById(R.id.start_btn);
        mAccountBtn = (Button) findViewById(R.id.account_btn);
        mReitingBtn = (Button) findViewById(R.id.reiting_btn);

        mMainText.setTextColor(getResources().getColor(R.color.app_name_in_menu));

        mAllTypeFace = Typeface.createFromAsset(getAssets(), "fonts/buttons_in_menu.ttf");
        mStartBtn.setTypeface(mAllTypeFace);
        mAccountBtn.setTypeface(mAllTypeFace);
        mReitingBtn.setTypeface(mAllTypeFace);
        mMainTypeFace = Typeface.createFromAsset(getAssets(), "fonts/app_name_in_menu.otf");
        mMainText.setTypeface(mMainTypeFace);
        mStartBtn.setOnClickListener(this);
        mReitingBtn.setOnClickListener(this);
    }


    @Override
    protected Dialog onCreateDialog(final int id) {
        switch (id) {
            case 1:

                final String[] mGameTypes = getResources().getStringArray(R.array.types_of_game);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose a game"); // заголовок для диалога

                builder.setItems(mGameTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 1) {
                            Log.e("mylog", "onlinegame");
                            MyTask myTask = new MyTask();
                            myTask.execute();
                        } else if (item == 0) {
                            Intent intent = new Intent(MainMenuActivity.this, LocateShipsActivity.class);
                            intent.putExtra("owner", "notonlinegame");
                            startActivity(intent);
                        }
                    }
                });
                builder.setCancelable(false);
                return builder.create();

            default:
                return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                showDialog(1);
                break;
            case R.id.reiting_btn:
                Intent intent = new Intent(MainMenuActivity.this, UsersRatingActivity.class);
                startActivity(intent);
                break;

        }
    }


    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("mylog", "start");
            mProgressDialog.show(MainMenuActivity.this, "",
                  "Looking for opponents. Please wait...", true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.e("mylog","end");
            Intent intent = new Intent(MainMenuActivity.this, LocateShipsActivity.class);
            intent.putExtra("owner", "onlinegame");
            startActivity(intent);

        }
    }


}
