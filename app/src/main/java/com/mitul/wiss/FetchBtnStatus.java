package com.mitul.wiss;

import android.app.Activity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class FetchBtnStatus {

    private ExtendedFloatingActionButton extendedFloatingActionButton;

    FetchBtnStatus(Activity activity){
        extendedFloatingActionButton = activity.findViewById(R.id.fetchBtn);
    }

    public void setToStart(){
        extendedFloatingActionButton.setText("Start");
    }

    public void setToFetch(){
        extendedFloatingActionButton.setText("Fetch");
    }

    public void setToStop(){
        extendedFloatingActionButton.setText("Stop");
    }

    public void setToMeasure(){
        extendedFloatingActionButton.setText("Measure");
    }
}
