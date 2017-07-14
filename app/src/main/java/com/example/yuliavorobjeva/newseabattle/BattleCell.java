package com.example.yuliavorobjeva.newseabattle;

import android.util.Pair;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by yuliavorobjeva on 11.07.17.
 */

public class BattleCell implements Serializable{
    private int mPosition;
    private Pair<Integer, Integer> mCoordinate;
    private State mState = State.EMPTY;


    public BattleCell(int position, State state) {
        mPosition = position;
        mState = state;
    }

    public int getPosition() {
        return mPosition;
    }

    public int getX() {
        return mCoordinate.first;
    }

    public int getY() {
        return mCoordinate.second;
    }

    public State getState() {
        return mState;
    }

    public enum State {
        EMPTY, SHIP, MISSED_SHOT, FIRE, UNKLICKABLE_FIRE, UNKLICKABLE_SHIP, UNKLICKABLE_MISSED, UNKLICKABLE_EMPTY
    }

    public void setState(State state) {
        if (state == State.EMPTY && (mState == State.SHIP|| mState == State.FIRE)) return; // early exit
        //if (state == State.FIRE && (mState == State.FIRE || mState == State.MISSED_SHOT||)) return; // early exit
        mState = state;
    }


    public static Comparator<BattleCell> CellComparator = new Comparator<BattleCell>() {

        public int compare(BattleCell cell1, BattleCell cell2) {
            int x1 = cell1.getX();
            int y1 = cell1.getY();
            int x2 = cell2.getX();
            int y2 = cell2.getY();
            if (x1 == x2 && y1 == y2) return 0;

            if (x1 == x2) {
                if (y1 < y2) return -1;
                else return 1;
            }

            if (y1 == y2) {
                if (x1 < x2) return -1;
                else return 1;
            }

            return 0;
        }

    };
}
