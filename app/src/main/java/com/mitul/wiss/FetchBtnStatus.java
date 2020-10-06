package com.mitul.wiss;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class FetchBtnStatus {

    private ExtendedFloatingActionButton extendedFloatingActionButton;
    private Drawable startIcon;
    private Drawable stopIcon;
    private Drawable fetchIcon;

    @SuppressLint("UseCompatLoadingForDrawables")
    FetchBtnStatus(Activity activity){
        extendedFloatingActionButton = activity.findViewById(R.id.fetchBtn);
        Resources resources = activity.getResources();
        startIcon = resources.getDrawable(R.drawable.baseline_play_circle_outline_black_18dp);
        stopIcon = resources.getDrawable(R.drawable.outline_stop_circle_black_18dp);
        fetchIcon = resources.getDrawable(R.drawable.baseline_wifi_black_18dp);
    }

    public void setToStart(){
        extendedFloatingActionButton.setText("Start");
        extendedFloatingActionButton.setIcon(startIcon);
    }

    public void setToFetch(){
        extendedFloatingActionButton.setText("Fetch");
        extendedFloatingActionButton.setIcon(fetchIcon);
    }

    public void setToStop(){
        extendedFloatingActionButton.setText("Stop");
        extendedFloatingActionButton.setIcon(stopIcon);
    }

    public void setToMeasure(){
        extendedFloatingActionButton.setText("Measure");
        extendedFloatingActionButton.setIcon(fetchIcon);
    }
}
