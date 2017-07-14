package com.example.yuliavorobjeva.newseabattle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

/**
 * Created by yuliavorobjeva on 13.07.17.
 */

public class BattleFieldForOnlineAdapter extends RecyclerView.Adapter<BattleFieldForOnlineAdapter.ViewHolder>{

    private static final int FIELD_SIZE = 100;

    private List<BattleCell> mCellList;
    private Context mContext;
    private OnBattleCellClickListener mOnCellClickListener;
    private int mWhich;
    private boolean mIsFilled;
    private boolean mInAvailabilityMode;

    public BattleFieldForOnlineAdapter(Context context, List<BattleCell> list, OnBattleCellClickListener onBattleCellClickListener, int which) {
        mContext = context;
        mCellList = list;
        mOnCellClickListener = onBattleCellClickListener;
        mWhich = which;
    }

    public void updateList(List<BattleCell> list) {
        mCellList = list;
        notifyDataSetChanged();
    }

    @Override
    public BattleFieldForOnlineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_battle_field, parent, false);
        return new BattleFieldForOnlineAdapter.ViewHolder((ImageButton) v);

    }

    @Override
    public void onBindViewHolder(BattleFieldForOnlineAdapter.ViewHolder holder, int position) {

        BattleCell cell = mCellList.get(position);
        boolean isClickable = false;
        int color = mContext.getResources().getColor(R.color.background_game_color);

        switch (cell.getState()) {
            case FIRE:
                color = mContext.getResources().getColor(android.R.color.black);
                isClickable = false;
                break;
            case MISSED_SHOT:
                color = mContext.getResources().getColor(R.color.unclicable_items_color);
                isClickable = false;
                break;
            case EMPTY:
                if (mIsFilled) color = mContext.getResources().getColor(R.color.unclicable_items_color);
                else  color = mContext.getResources().getColor(R.color.background_game_color);
                isClickable = !mInAvailabilityMode;
                break;
            case SHIP:
                color = mContext.getResources().getColor(R.color.lines_for_battle_field);
                isClickable = true;
                break;
            case UNKLICKABLE_EMPTY:
                isClickable = false;
                break;
            case UNKLICKABLE_FIRE:
                isClickable = false;
                break;
            case UNKLICKABLE_MISSED:
                isClickable = false;
                break;
            case UNKLICKABLE_SHIP:
                isClickable = false;
                break;
        }

        holder.hImageButton.setBackgroundColor(color);
        holder.hImageButton.setEnabled(!mIsFilled && isClickable);

        final int finalPosition = position;
        holder.hImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCellClickListener != null) {
                    mOnCellClickListener.OnBattleCellClick(finalPosition, mWhich);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return FIELD_SIZE;
    }

    public void setFilled(boolean isFilled) {
        mIsFilled = isFilled;
    }

    public void setInAvailabilityMode(boolean inAvailabilityMode) {
        mInAvailabilityMode = inAvailabilityMode;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton hImageButton;

        public ViewHolder(ImageButton imageButton) {
            super(imageButton);
            hImageButton = imageButton;
        }
    }
}

