package com.example.yuliavorobjeva.newseabattle;

/**
 * Created by yuliavorobjeva on 06.07.17.
 */

public class PersonsScore {

    private String mPersonName;
    private String mPersonScore;
    private int mRatingPlase;

    public PersonsScore(String name, String score, int rating) {
        mPersonName = name;
        mPersonScore = score;
        mRatingPlase = rating;
    }

    public String getPersonName() {
        return mPersonName;
    }

    public String getPersonScore() {
        return mPersonScore;
    }

    public int getRatingPlase() {
        return mRatingPlase;
    }
}
