package com.example.yuliavorobjeva.newseabattle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.yuliavorobjeva.newseabattle.Cell.CellComparator;

public class CellsController {

    private List<Cell> mCells;
    private int mFourDeckCount;
    private int mThreeDeckCount;
    private int mTwoDeckCount;
    private int mOneDeckCount;
    private ShipType mShipType;
    private List<Cell> mCurrentShip;

    public CellsController() {
        reset();
    }

    public ShipType getShipType() {
        return mShipType;
    }

    public int getFourDeckCount() {
        return mFourDeckCount;
    }

    public int getThreeDeckCount() {
        return mThreeDeckCount;
    }

    public int getTwoDeckCount() {
        return mTwoDeckCount;
    }

    public int getOneDeckCount() {
        return mOneDeckCount;
    }

    public List<Cell> getCells() {
        return mCells;
    }

    public void reset() {
        mCells = new ArrayList<>();
        mCurrentShip = new ArrayList<>();
        for (int i = 0; i < (Configurator.FIELD_SIDE_SIZE * Configurator.FIELD_SIDE_SIZE); i++) {
            mCells.add(new Cell(i, getCoordinateForPosition(i)));
        }
        mFourDeckCount = Configurator.FOUR_DECK_COUNT;
        mThreeDeckCount = Configurator.THREE_DECK_COUNT;
        mTwoDeckCount = Configurator.TWO_DECK_COUNT;
        mOneDeckCount = Configurator.ONE_DECK_COUNT;
        mShipType = ShipType.ONE_DECK;
    }

    private IntegerPair getCoordinateForPosition(int position) {
        int x = position / Configurator.FIELD_SIDE_SIZE;
        int y = position % Configurator.FIELD_SIDE_SIZE;
        return new IntegerPair(x, y);
    }

    private int getPositionForCoordinate(int x, int y) {
        return x * Configurator.FIELD_SIDE_SIZE + y;
    }

    public boolean isInAvailabilityMode() {
        return mCurrentShip.size() != 0;
    }

    public void performClick(int position) {
        Cell cell = mCells.get(position);
        switch (cell.getState()) {
            case EMPTY:
            case AVAILABLE:
                cell.setState(Cell.State.FILLED);
                mCurrentShip.add(cell);
                calculateNearField(cell);
                break;
        }
    }

    private void calculateNearField(Cell cell) {
        if (mCurrentShip.size() == mShipType.getSize()) {
            switch (mShipType) {
                case ONE_DECK:
                    mOneDeckCount--;
                    if (mOneDeckCount == 0) {
                        mShipType = ShipType.TWO_DECK;
                    }
                    break;
                case TWO_DECK:
                    mTwoDeckCount--;
                    if (mTwoDeckCount == 0) {
                        mShipType = ShipType.THREE_DECK;
                    }
                    break;
                case THREE_DECK:
                    mThreeDeckCount--;
                    if (mThreeDeckCount == 0) {
                        mShipType = ShipType.FOUR_DECK;
                    }
                    break;
                case FOUR_DECK:
                    mFourDeckCount--;
                    if (mFourDeckCount == 0) {
                        mShipType = ShipType.NOT_DEFINED;
                    }
                    break;
            }

            Collections.sort(mCurrentShip, CellComparator);

            Cell firstCell = mCurrentShip.get(0);
            Cell lastCell = mCurrentShip.get(mCurrentShip.size() - 1);

            int fromX = firstCell.getX();
            int toX = lastCell.getX();
            int fromY = firstCell.getY();
            int toY = lastCell.getY();

            for (int x = (fromX - 1) >= 0 ? (fromX - 1) : 0;
                 x <= ((toX + 1) <= (Configurator.FIELD_SIDE_SIZE - 1) ? (toX + 1) : (Configurator.FIELD_SIDE_SIZE - 1));
                 x++) {
                for (int y = (fromY - 1) >= 0 ? (fromY - 1) : 0;
                     y <= ((toY + 1) <= (Configurator.FIELD_SIDE_SIZE - 1) ? (toY + 1) : (Configurator.FIELD_SIDE_SIZE - 1));
                     y++) {
                    Cell currentCell = mCells.get(getPositionForCoordinate(x, y));
                    if (currentCell.getState() == Cell.State.EMPTY
                            || currentCell.getState() == Cell.State.AVAILABLE) {
                        currentCell.setState(Cell.State.BLOCKED);
                    }
                }
            }
            mCurrentShip.clear();
            return; // early exit
        }

        if (mCurrentShip.size() == 1) {
            int x = cell.getX();
            int y = cell.getY();
            if (checkHorizontal(cell, mShipType.getSize() - mCurrentShip.size())) {
                mCells.get(getPositionForCoordinate(x, (y + 1) <= (Configurator.FIELD_SIDE_SIZE - 1) ? (y + 1) : (Configurator.FIELD_SIDE_SIZE - 1)))
                        .setState(Cell.State.AVAILABLE);
                mCells.get(getPositionForCoordinate(x, (y - 1) >= 0 ? (y - 1) : 0))
                        .setState(Cell.State.AVAILABLE);
            }
            if (checkVertical(cell, mShipType.getSize() - mCurrentShip.size())) {
                mCells.get(getPositionForCoordinate((x + 1) <= (Configurator.FIELD_SIDE_SIZE - 1) ? (x + 1) : (Configurator.FIELD_SIDE_SIZE - 1), y))
                        .setState(Cell.State.AVAILABLE);
                mCells.get(getPositionForCoordinate((x - 1) >= 0 ? (x - 1) : 0, y))
                        .setState(Cell.State.AVAILABLE);
            }
        } else {
            Collections.sort(mCurrentShip, CellComparator);

            Cell firstCell = mCurrentShip.get(0);
            Cell lastCell = mCurrentShip.get(mCurrentShip.size() - 1);

            int fromX = firstCell.getX();
            int toX = lastCell.getX();
            int fromY = firstCell.getY();
            int toY = lastCell.getY();

            if (fromX == toX) {
                mCells.get(getPositionForCoordinate(toX, (toY + 1) <= (Configurator.FIELD_SIDE_SIZE - 1) ? (toY + 1) : (Configurator.FIELD_SIDE_SIZE - 1)))
                        .setState(Cell.State.AVAILABLE);
                mCells.get(getPositionForCoordinate(fromX, (fromY - 1) >= 0 ? (fromY - 1) : 0))
                        .setState(Cell.State.AVAILABLE);
                for (Cell existCell : mCurrentShip) {
                    mCells.get(getPositionForCoordinate((existCell.getX() - 1) >= 0 ? (existCell.getX() - 1) : 0, existCell.getY()))
                            .setState(Cell.State.EMPTY);
                    mCells.get(getPositionForCoordinate((existCell.getX() + 1) <= (Configurator.FIELD_SIDE_SIZE - 1) ? (existCell.getX() + 1) : (Configurator.FIELD_SIDE_SIZE - 1), existCell.getY()))
                            .setState(Cell.State.EMPTY);
                }
            }

            if (fromY == toY) {
                mCells.get(getPositionForCoordinate((fromX - 1) >= 0 ? (fromX - 1) : 0, fromY))
                        .setState(Cell.State.AVAILABLE);
                mCells.get(getPositionForCoordinate((toX + 1) <= (Configurator.FIELD_SIDE_SIZE - 1) ? (toX + 1) : (Configurator.FIELD_SIDE_SIZE - 1), toY))
                        .setState(Cell.State.AVAILABLE);
                for (Cell existCell : mCurrentShip) {
                    mCells.get(getPositionForCoordinate(existCell.getX(), (existCell.getY() + 1) <= (Configurator.FIELD_SIDE_SIZE - 1) ? (existCell.getY() + 1) : (Configurator.FIELD_SIDE_SIZE - 1)))
                            .setState(Cell.State.EMPTY);
                    mCells.get(getPositionForCoordinate(existCell.getX(), (existCell.getY() - 1) >= 0 ? (existCell.getY() - 1) : 0))
                            .setState(Cell.State.EMPTY);
                }
            }
        }
    }

    private boolean checkHorizontal(Cell cell, int steps) {
        if (checkLeftSteps(cell, steps) + checkRightSteps(cell, steps) >= steps) return true;
        return false;
    }

    private boolean checkVertical(Cell cell, int steps) {
        if (checkBottomSteps(cell, steps) + checkTopSteps(cell, steps) >= steps) return true;
        return false;
    }

    private int checkLeftSteps(Cell cell, int steps) {
        int result = 0;
        for (int i = 1; i <= steps; i++) {
            if (cell.getY() - i < 0) return i - 1;
            Cell.State state = mCells.get(getPositionForCoordinate(cell.getX(), cell.getY() - i)).getState();
            if (state == Cell.State.BLOCKED) return i - 1;
            result = i;
        }
        return result;
    }

    private int checkRightSteps(Cell cell, int steps) {
        int result = 0;
        for (int i = 1; i <= steps; i++) {
            if (cell.getY() + i > Configurator.FIELD_SIDE_SIZE - 1) return i - 1;
            Cell.State state = mCells.get(getPositionForCoordinate(cell.getX(), cell.getY() + i)).getState();
            if (state == Cell.State.BLOCKED) return i - 1;
            result = i;
        }
        return result;
    }

    private int checkTopSteps(Cell cell, int steps) {
        int result = 0;
        for (int i = 1; i <= steps; i++) {
            if (cell.getX() - i < 0) return i - 1;
            Cell.State state = mCells.get(getPositionForCoordinate(cell.getX() - i, cell.getY())).getState();
            if (state == Cell.State.BLOCKED) return i - 1;
            result = i;
        }
        return result;
    }

    private int checkBottomSteps(Cell cell, int steps) {
        int result = 0;
        for (int i = 1; i <= steps; i++) {
            if (cell.getX() + i > Configurator.FIELD_SIDE_SIZE - 1) return i - 1;
            Cell.State state = mCells.get(getPositionForCoordinate(cell.getX() + i, cell.getY())).getState();
            if (state == Cell.State.BLOCKED) return i - 1;
            result = i;
        }
        return result;
    }

    public enum ShipType {
        NOT_DEFINED(0),
        ONE_DECK(1),
        TWO_DECK(2),
        THREE_DECK(3),
        FOUR_DECK(4);

        private int size_;

        ShipType(int size) {
            size_ = size;
        }

        public int getSize() {
            return size_;
        }
    }

    public static class Configurator {
        public static final int FIELD_SIDE_SIZE = 10;
        public static final int FOUR_DECK_COUNT = 1;
        public static final int THREE_DECK_COUNT = 2;
        public static final int TWO_DECK_COUNT = 3;
        public static final int ONE_DECK_COUNT = 4;
    }

    public Boolean isAllNotEmpty(){ //not worked
        for (int i = 0; i < mCells.size(); i++) {
            if(mCells.get(i).getState() == Cell.State.AVAILABLE){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> makeArr(){
        ArrayList<Integer> array = new ArrayList<>();
        for(int i =0; i < mCells.size(); i++) {
            if (mCells.get(i).getState() == Cell.State.FILLED) {
                array.add(1);
            } else {
                array.add(0);
            }
        }
        return array;
    }
}
