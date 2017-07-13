package com.example.yuliavorobjeva.newseabattle;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class LocateShipsActivity extends AppCompatActivity implements OnCellClickListener {

    private RecyclerView mRecyclerView;
    private BattleFieldAdapter mBattleFieldAdapter;
    private ImageButton mOneShipBtn;
    private ImageButton mTwoShipBtn;
    private ImageButton mThreeShipBtn;
    private ImageButton mFourShipBtn;
    private TextView mOneShipCount;
    private TextView mTwoShipCount;
    private TextView mThreeShipCount;
    private TextView mFourShipCount;
    private Button mResetBtn;
    private Button mOkBtn;
    private Typeface mTypeface;
    private Typeface mTypeFaceForCounters;
    private TextView mPlaceShipsTv;
    private CellsController mCellsController;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_ships);

        mCellsController = new CellsController();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mOneShipBtn = (ImageButton) findViewById(R.id.one_ship_btn);
        mTwoShipBtn = (ImageButton) findViewById(R.id.two_ship_btn);
        mThreeShipBtn = (ImageButton) findViewById(R.id.three_ship_btn);
        mFourShipBtn = (ImageButton) findViewById(R.id.fourth_ship_btn);
        mOneShipCount = (TextView) findViewById(R.id.one_ship_counter);
        mTwoShipCount = (TextView) findViewById(R.id.two_ship_counter);
        mThreeShipCount = (TextView) findViewById(R.id.three_ship_counter);
        mFourShipCount = (TextView) findViewById(R.id.four_ship_counter);
        mResetBtn = (Button) findViewById(R.id.reset_btn);
        mOkBtn = (Button) findViewById(R.id.ok_btn);
        mPlaceShipsTv = (TextView) findViewById(R.id.place_ships_tv);

        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/buttons_in_menu.ttf");
        mTypeFaceForCounters = Typeface.createFromAsset(getAssets(), "fonts/main_font.ttf");
        mOkBtn.setTypeface(mTypeface);
        mResetBtn.setTypeface(mTypeface);
        mPlaceShipsTv.setTypeface(mTypeface);
        mOneShipCount.setTypeface(mTypeFaceForCounters);
        mTwoShipCount.setTypeface(mTypeFaceForCounters);
        mThreeShipCount.setTypeface(mTypeFaceForCounters);
        mFourShipCount.setTypeface(mTypeFaceForCounters);

        intent = getIntent();




        updateShipsCounter();

        mBattleFieldAdapter = new BattleFieldAdapter(this, mCellsController.getCells(), this);
        mRecyclerView.setAdapter(mBattleFieldAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, CellsController.Configurator.FIELD_SIDE_SIZE));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    @Override
    public void onCellClick(int position) {
        mCellsController.performClick(position);
        update();
    }

    public void onResetClick(View view) {
        mCellsController.reset();
        update();
    }

    private void update() {
        updateShipsCounter();
        updateShipsIcons();
        mBattleFieldAdapter.setInAvailabilityMode(mCellsController.isInAvailabilityMode());
        mBattleFieldAdapter.setFilled(mCellsController.getShipType() == CellsController.ShipType.NOT_DEFINED);
        mBattleFieldAdapter.updateList(mCellsController.getCells());

    }

    private void updateShipsCounter() {
        mFourShipCount.setText(String.valueOf(mCellsController.getFourDeckCount()));
        mThreeShipCount.setText(String.valueOf(mCellsController.getThreeDeckCount()));
        mTwoShipCount.setText(String.valueOf(mCellsController.getTwoDeckCount()));
        mOneShipCount.setText(String.valueOf(mCellsController.getOneDeckCount()));
    }

    private void updateShipsIcons() {
        mFourShipBtn.setBackground(getDrawable(R.drawable.forthship_unselected));
        mThreeShipBtn.setBackground(getDrawable(R.drawable.thirdship_unselected));
        mTwoShipBtn.setBackground(getDrawable(R.drawable.secondship_unselected));
        mOneShipBtn.setBackground(getDrawable(R.drawable.firstship_unselected));
        switch (mCellsController.getShipType()) {
            case FOUR_DECK:
                mFourShipBtn.setBackground(getDrawable(R.drawable.forthship));
                break;
            case THREE_DECK:
                mThreeShipBtn.setBackground(getDrawable(R.drawable.thirdship));
                break;
            case TWO_DECK:
                mTwoShipBtn.setBackground(getDrawable(R.drawable.secondship));
                break;
            case ONE_DECK:
                mOneShipBtn.setBackground(getDrawable(R.drawable.firstship));
                break;
        }
    }

    public void onOkClick(View view){
            reload();
    }

    private void reload() {
        if( intent.hasExtra("owner")) {
            if(intent.getExtras().get("owner").equals("onlinegame")) {
                Log.e("mylog", "was started for online.will write new activity and MAKE FIREBASE INFO");
                Intent intent1 = new Intent(LocateShipsActivity.this, LogoActivity.class);//CHANGE ACTIVITY
                intent1.putExtra("positions1", (Serializable) mCellsController.getCells());
                Log.e("sended info to activity", mCellsController.makeArr().toString());
                startActivity(intent1);

            } else if (intent.getExtras().get("owner").equals("notonlinegame")) {
                if(intent.getSerializableExtra("positions1")==null) {
                    intent.putExtra("positions1", (Serializable) mCellsController.getCells());
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                } else {
                    List<Cell> cells = (List<Cell>) intent.getSerializableExtra("positions1");
                    Intent newIntent = new Intent(LocateShipsActivity.this, GameForTwoActivity.class);
                    Log.e("second", String.valueOf(mCellsController.makeArr()));
                    newIntent.putExtra("positions1", (Serializable) cells);
                    newIntent.putExtra("positions2", (Serializable) mCellsController.getCells());
                    startActivity(newIntent);

                }
            }

        }

    }
}