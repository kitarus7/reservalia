package com.kitarsoft.reservalia.utils;

public class Scale {

    public static float scaleValues(float maxAllowed, float minAllowed, float minInput, float maxInpout, float value){
        return (maxAllowed - minAllowed) * (value - minInput) / (maxInpout - minInput) + minAllowed;
    }

}
