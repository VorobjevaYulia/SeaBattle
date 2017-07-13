package com.example.yuliavorobjeva.newseabattle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameForTwoActivity extends AppCompatActivity implements OnBattleCellClickListener {

    private final static int INFO_DIALOG = 1;
    private final static int WINNER_FIRST_DIALOG = 2;
    private final static int WINNER_SECOND_DIALOG = 3;

    private RecyclerView mRecyclerForFirst;
    private RecyclerView mRecyclerForSecond;

    private TextView mFirstTV;
    private TextView mSecondTV;
    private Typeface mTypeface;


    private List<Cell> mCellsFirst;
    private List<Cell> mCellsSecond;
    private List<BattleCell> mBattleCellFirst;
    private List<BattleCell> mBattleCellSecond;

    private BattleFieldForTwoAdapter mBattleFieldAdapterForFirst;
    private BattleFieldForTwoAdapter mBattleFieldAdapterForSecond;

    private BattleCellsController mBattleCellsControllerForFirst;
    private BattleCellsController mBattleCellsControllerForSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_for_two);
        showDialog(1);

        mFirstTV = (TextView) findViewById(R.id.first_tv);
        mSecondTV = (TextView) findViewById(R.id.second_tv);
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/main_font.ttf");
        mFirstTV.setTypeface(mTypeface);
        mSecondTV.setTypeface(mTypeface);



        mBattleCellFirst = new ArrayList<>();
        mBattleCellSecond = new ArrayList<>();

        mCellsFirst = (List<Cell>) getIntent().getSerializableExtra("positions1");
        mCellsSecond = (List<Cell>) getIntent().getSerializableExtra("positions2");


        for (int i = 0; i < mCellsFirst.size(); i++) {
            if (mCellsFirst.get(i).getState() == Cell.State.FILLED) {
                mBattleCellFirst.add(new BattleCell(i, BattleCell.State.SHIP));
            } else {
                mBattleCellFirst.add(new BattleCell(i, BattleCell.State.EMPTY));
            }
        }

        for (int i = 0; i < mCellsSecond.size(); i++) {
            if (mCellsSecond.get(i).getState() == Cell.State.FILLED) {
                mBattleCellSecond.add(new BattleCell(i, BattleCell.State.SHIP));
            } else {
                mBattleCellSecond.add(new BattleCell(i, BattleCell.State.EMPTY));
            }
        }


        mBattleCellsControllerForFirst = new BattleCellsController(mBattleCellFirst);
        mBattleCellsControllerForSecond = new BattleCellsController(mBattleCellSecond);


        mRecyclerForFirst = (RecyclerView) findViewById(R.id.recyclerViewForFirst);
        mRecyclerForSecond = (RecyclerView) findViewById(R.id.recyclerViewForSecond);

        mBattleFieldAdapterForFirst = new BattleFieldForTwoAdapter(this, mBattleCellsControllerForFirst.getCells(), this, 1);
        mBattleFieldAdapterForSecond = new BattleFieldForTwoAdapter(this, mBattleCellsControllerForSecond.getCells(), this, 2);
        mRecyclerForFirst.setAdapter(mBattleFieldAdapterForFirst);
        mRecyclerForSecond.setAdapter(mBattleFieldAdapterForSecond);

        mRecyclerForFirst.setLayoutManager(new GridLayoutManager(this, 10));
        mRecyclerForSecond.setLayoutManager(new GridLayoutManager(this, 10));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerForFirst.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        mRecyclerForSecond.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


    }


    @Override
    public void OnBattleCellClick(int position, int which) {
        if (which == 1) {
            mBattleCellsControllerForFirst.performClick(position);
            mBattleFieldAdapterForFirst.updateList(mBattleCellsControllerForFirst.getCells());
            if (isWinner(mBattleCellsControllerForFirst.getCells())) {
                Log.e("firts win", "first");
                showDialog(WINNER_FIRST_DIALOG);

            }
        } else if (which == 2) {
            mBattleCellsControllerForSecond.performClick(position);
            mBattleFieldAdapterForSecond.updateList(mBattleCellsControllerForSecond.getCells());
            if (isWinner(mBattleCellsControllerForSecond.getCells())) {
                Log.e("second win", "second");
                showDialog(WINNER_SECOND_DIALOG);

            }
        }
    }

    private boolean isWinner(List<BattleCell> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getState() == BattleCell.State.SHIP) {
                return false;
            }
        }
        return true;
    }

    protected Dialog onCreateDialog(int id) {
        if (id == INFO_DIALOG) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Please, read info");
            adb.setMessage("Purple color - you crashed a ship \n Grey color - you missed");
            adb.setIcon(android.R.drawable.ic_dialog_info);
            adb.setNegativeButton("Ok", infoDialogListener);
            return adb.create();
        } else if( id == WINNER_FIRST_DIALOG) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("WE HAVE A WINNER!:)");
            adb.setMessage("Second player won! Congratulations!");
            adb.setIcon(R.drawable.salut);
            adb.setPositiveButton("Ok", winnerDialogListener);
            adb.setCancelable(false);
            return adb.create();
        } else if( id == WINNER_SECOND_DIALOG) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("WE HAVE A WINNER!:)");
            adb.setMessage("First player won! Congratulations!");
            adb.setIcon(R.drawable.salut);
            adb.setPositiveButton("Ok", winnerDialogListener);
            adb.setCancelable(false);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener infoDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if(i == Dialog.BUTTON_NEGATIVE) {
            }
        }
    };

    DialogInterface.OnClickListener winnerDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if(i == Dialog.BUTTON_POSITIVE) {
                Intent intent = new Intent(GameForTwoActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        }
    };
}
