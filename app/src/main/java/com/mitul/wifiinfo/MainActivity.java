package com.mitul.wifiinfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private AppFields appFields;
    private WifiManager wifiManager;
    private MeasurementMode measurementMode;
    private FetchBtnStatus fetchBtnStatus;
    private WiFiWorkflows wiFiWorkflows;

    private InstantaneousModeExecutor instantaneousModeExecutor;
    private ContinuousModeExecutor continuousModeExecutor;
    private BurstModeExecutor burstModeExecutor;
    private IModeExector currentModeExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        measurementMode = MeasurementMode.INSTANTANEOUS;
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wiFiWorkflows = new WiFiWorkflows(wifiManager, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mode, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(measurementMode != MeasurementMode.INSTANTANEOUS){
            Log.d("MenuItemSelect", "Stopped Prev Execution");
            currentModeExecutor.stopExecution();
        }

        switch (item.getItemId()){
            case R.id.menu_instant:
                fetchBtnStatus.setToMeasure();
                currentModeExecutor = instantaneousModeExecutor;
                measurementMode = MeasurementMode.INSTANTANEOUS;
                break;

            case R.id.menu_cont:
                fetchBtnStatus.setToStart();
                currentModeExecutor = continuousModeExecutor;
                measurementMode = MeasurementMode.CONTINUOUS;
                break;

            case R.id.menu_burst:
                fetchBtnStatus.setToStart();
                currentModeExecutor = burstModeExecutor;
                measurementMode = MeasurementMode.BURST;
                break;
        }

        item.setChecked(true);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        appFields = new AppFields(this);
        fetchBtnStatus = new FetchBtnStatus(this);

        instantaneousModeExecutor = new InstantaneousModeExecutor(appFields, wifiManager);
        continuousModeExecutor = new ContinuousModeExecutor(appFields, wifiManager, fetchBtnStatus);
        burstModeExecutor = new BurstModeExecutor(appFields, wifiManager, fetchBtnStatus);
        currentModeExecutor = instantaneousModeExecutor;
    }

    @Override
    protected void onStop() {
        super.onStop();
        currentModeExecutor.stopExecution();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onFetchClick(@Nullable View v){
        Log.d("FetchBtn", "Clicked");
        Log.d("CurrentMode", measurementMode.toString());
        if(wiFiWorkflows.isWiFiConnected()){
            currentModeExecutor.execute();
        } else {
            wiFiWorkflows.showWiFiNotConnected();
        }
    }
}