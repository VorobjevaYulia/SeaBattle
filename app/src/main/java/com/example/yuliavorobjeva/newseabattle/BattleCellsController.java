package com.example.yuliavorobjeva.newseabattle;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuliavorobjeva on 11.07.17.
 */

public class BattleCellsController {

    private List<BattleCell> mCells;

    public BattleCellsController(List<BattleCell> list){
        reset(list);
    }

    public void reset(List<BattleCell> list) {
        mCells = new ArrayList<>();
        for (int i = 0; i < (CellsController.Configurator.FIELD_SIDE_SIZE * CellsController.Configurator.FIELD_SIDE_SIZE); i++) {
            mCells.add(new BattleCell(i, list.get(i).getState()));
            Log.e("RESET", "BATTLECELLSCONTROLLER");
        }

    }

    public void performClick(int position) {
        BattleCell cell = mCells.get(position);
        switch (cell.getState()) {
            case EMPTY:
                cell.setState(BattleCell.State.MISSED_SHOT);
                Log.e("PerformClick",String.valueOf(cell.getPosition()));
                break;
            case SHIP:
                cell.setState(BattleCell.State.FIRE);
                Log.e("performClick", "BattleCellControllerSHIP");
                break;
        }
    }



    public List<BattleCell> getCells(){
        return mCells;
    }
}
