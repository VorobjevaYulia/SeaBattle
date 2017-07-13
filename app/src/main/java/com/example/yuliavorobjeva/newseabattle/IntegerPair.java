package com.example.yuliavorobjeva.newseabattle;

import java.io.Serializable;

/**
 * Created by yuliavorobjeva on 11.07.17.
 */

public class IntegerPair implements Serializable{

    public Integer first;
    public Integer second;

    public IntegerPair (Integer first, Integer second) {
        this.first = first;
        this.second = second;
    }
}
