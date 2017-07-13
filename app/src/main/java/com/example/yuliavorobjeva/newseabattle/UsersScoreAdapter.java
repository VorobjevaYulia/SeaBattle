package com.example.yuliavorobjeva.newseabattle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

/**
 * Created by yuliavorobjeva on 06.07.17.
 */

public class UsersScoreAdapter extends RecyclerView.Adapter<UsersScoreAdapter.PersonViewHolder> {

    List<PersonsScore> mPersons;
    Context mContext;


    public UsersScoreAdapter(Context context, List<PersonsScore> info) {
        mContext = context;
        mPersons = info;
    }

    @Override
    public UsersScoreAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_score_item, parent, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UsersScoreAdapter.PersonViewHolder holder, int position) {
        PersonsScore mPersonScore = mPersons.get(position);
        holder.hPersonName.setText(mPersonScore.getPersonName());
        holder.hPersonScore.setText(mPersonScore.getPersonScore());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int score = mPersonScore.getRatingPlase();
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.BLACK)
                .useFont(Typeface.DEFAULT)
                .fontSize(80) /* size in px */
                .bold()
                .textColor(R.color.logoStatusBar)
                .toUpperCase()
                .endConfig()
                .buildRound(String.valueOf(score), generator.getColor(score));
        holder.hPersonRatingPlace.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView hPersonName;
        TextView hPersonScore;
        ImageView hPersonRatingPlace;


        public PersonViewHolder(View itemView) {
            super(itemView);
            this.hPersonName = itemView.findViewById(R.id.person_name_tv);
            this.hPersonScore = itemView.findViewById(R.id.person_score_tv);
            this.hPersonRatingPlace = itemView.findViewById(R.id.rating_place_image);
        }
    }
}
