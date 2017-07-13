package com.example.yuliavorobjeva.newseabattle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

public class BattleFieldAdapter extends RecyclerView.Adapter<BattleFieldAdapter.ViewHolder> {
    
    private static final int FIELD_SIZE = 100;

    private List<Cell> mCellList;
    private Context mContext;
    private OnCellClickListener mOnCellClickListener;
    private boolean mIsFilled;
    private boolean mInAvailabilityMode;

    public BattleFieldAdapter(Context context, List<Cell> list, OnCellClickListener onCellClickListener){
        mCellList = list;
        mContext = context;
        mOnCellClickListener = onCellClickListener;
    }

    public void updateList(List<Cell> list){
        mCellList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_battle_field, parent, false);
        return new ViewHolder((ImageButton) v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Cell cell = mCellList.get(position);
        boolean isClickable = false;
        int color = mContext.getResources().getColor(R.color.logoStatusBar);
        switch (cell.getState()) {
            case AVAILABLE:
                color = mContext.getResources().getColor(R.color.next_step_color);
                isClickable = true;
                break;
            case BLOCKED:
                color = mContext.getResources().getColor(R.color.unclicable_items_color);
                isClickable = false;
                break;
            case EMPTY:
                if (mIsFilled) color = mContext.getResources().getColor(R.color.unclicable_items_color);
                else  color = mContext.getResources().getColor(R.color.logoStatusBar);
                isClickable = !mInAvailabilityMode;
                break;
            case FILLED:
                color = mContext.getResources().getColor(R.color.lines_for_battle_field);
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
                    mOnCellClickListener.onCellClick(finalPosition);
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
