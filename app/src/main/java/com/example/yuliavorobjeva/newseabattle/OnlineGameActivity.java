package com.example.yuliavorobjeva.newseabattle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class OnlineGameActivity extends AppCompatActivity {

    Intent intent;
    private List<Cell> mMyCells;
    private List<BattleCell> mMyBattleCells;
    private List<BattleCell> mOppositBattleCells;
    private List<Integer> mMyIntCells;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);

        intent = getIntent();
        mMyCells = (List<Cell>) intent.getSerializableExtra("positions1");
        for (int i = 0; i < mMyCells.size(); i++) {
            if (mMyCells.get(i).getState() == Cell.State.FILLED) {
                mMyBattleCells.add(new BattleCell(i, BattleCell.State.SHIP));
            } else {
                mMyBattleCells.add(new BattleCell(i, BattleCell.State.EMPTY));
            }
        }



    }
}
