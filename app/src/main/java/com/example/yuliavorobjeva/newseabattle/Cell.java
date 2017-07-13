package com.example.yuliavorobjeva.newseabattle;

import java.io.Serializable;
import java.util.Comparator;

public class Cell implements Serializable{

    private int mPosition;
    private IntegerPair mCoordinate;
    private State mState = State.EMPTY;

    public Cell(int position, IntegerPair coordinate) {
        mPosition = position;
        mCoordinate = coordinate;
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

    public void setState(State state) {
        if (state == State.AVAILABLE && (mState == State.BLOCKED || mState == State.FILLED)) return; // early exit
        if (state == State.EMPTY && (mState == State.BLOCKED || mState == State.FILLED)) return; // early exit
        mState = state;
    }

    public enum State implements Serializable{
        EMPTY, BLOCKED, FILLED, AVAILABLE
    }

    public static Comparator<Cell> CellComparator = new Comparator<Cell>() {

        public int compare(Cell cell1, Cell cell2) {
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
