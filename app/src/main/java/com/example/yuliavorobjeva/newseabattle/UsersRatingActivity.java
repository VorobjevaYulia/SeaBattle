package com.example.yuliavorobjeva.newseabattle;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UsersRatingActivity extends AppCompatActivity {

    private RecyclerView mUsers;
    private Button mFindMeBtn;
    private TextView mTopTV;
    private Typeface mTypeface;

    private static List<PersonsScore> mPersonsInfo;
    static {
        mPersonsInfo = new ArrayList<>();
        PersonsScore personInfo1 = new PersonsScore("Petya Ivanov", String.valueOf(5),1);
        mPersonsInfo.add(personInfo1);
        PersonsScore personInfo2 = new PersonsScore("Vasya", String.valueOf(5),2);
        mPersonsInfo.add(personInfo2);
        PersonsScore personInfo3 = new PersonsScore("Kotya", String.valueOf(5),3);
        mPersonsInfo.add(personInfo3);
        PersonsScore personInfo4 = new PersonsScore("Serega", String.valueOf(5),4);
        mPersonsInfo.add(personInfo4);
        PersonsScore personInfo5 = new PersonsScore("Petya Ivanov", String.valueOf(5),5);
        mPersonsInfo.add(personInfo5);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_rating);

        mUsers = (RecyclerView) findViewById(R.id.recycler);
        mFindMeBtn = (Button) findViewById(R.id.find_me_btn);
        mTopTV = (TextView) findViewById(R.id.top_users);
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/main_font.ttf");
        mFindMeBtn.setTypeface(mTypeface);
        mTopTV.setTypeface(mTypeface);


        UsersScoreAdapter usersScoreAdapter = new UsersScoreAdapter(this, mPersonsInfo);
        mUsers.setLayoutManager(new LinearLayoutManager(this));
        mUsers.setAdapter(usersScoreAdapter);

    }
}
