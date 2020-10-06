package com.mitul.wiss;

import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class ContinuousModeExecutor implements IModeExector{

    private HandlerThread handlerThread;
    private Handler handler;
    private AppFields appFields;
    private WifiManager wifiManager;
    private FetchBtnStatus fetchBtnStatus;

    private boolean isRunning;

    private String ssid;
    private String mac;
    private String linkSpeed;
    private String frequency;
    private String bssid;
    private String signalScore;
    private String rssi;

    ContinuousModeExecutor(AppFields _appFields, WifiManager _wifiManager, FetchBtnStatus _fetchBtnStatus){
        appFields = _appFields;
        wifiManager = _wifiManager;
        isRunning = false;
        fetchBtnStatus = _fetchBtnStatus;
    }

    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void fetchData(){
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        ssid = wifiInfo.getSSID().replace('\"', ' ');
        mac = wifiInfo.getMacAddress();
        linkSpeed = String.valueOf(wifiInfo.getLinkSpeed())+" "+WifiInfo.LINK_SPEED_UNITS;
        frequency = String.valueOf(wifiInfo.getFrequency())+" "+WifiInfo.FREQUENCY_UNITS;
        bssid = wifiInfo.getBSSID();
        rssi = String.valueOf(wifiInfo.getRssi())+" dBm";
        double score = (wifiInfo.getRssi()+127)*100/(max_rssi_dbm+127);
        signalScore = String.valueOf(Math.round(score))+"%";
    }

    private void uploadToUI(){
        appFields.ssid.setText(ssid);
        appFields.mac.setText(mac);
        appFields.linkSpeed.setText(linkSpeed);
        appFields.frequency.setText(frequency);
        appFields.bssid.setText(bssid);
        appFields.signalScore.setText(signalScore);
        appFields.rssi.setText(rssi);
    }

    private void fetchRssiOnly(){
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        rssi = String.valueOf(wifiInfo.getRssi())+" dBm";
        double score = (wifiInfo.getRssi()+127)*100/(max_rssi_dbm+127);
        signalScore = String.valueOf(Math.round(score))+"%";
    }

    private void uploadRssiToUI(){
        appFields.setSignalScore(signalScore);
        appFields.setRssi(rssi);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void execute() {
        if(isRunning){
            stopExecution();
        } else {
            fetchBtnStatus.setToStop();
            isRunning = true;
            handlerThread = new HandlerThread("ContUpdateWiSSThread");
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            handler = new Handler(looper);

            fetchData();
            uploadToUI();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchRssiOnly();
                    uploadRssiToUI();
                    Log.i("ContExecLoop:", "Running");
                    handler.postDelayed(this, 10);
                }
            }, 10);
        }
        Log.i("ContExecution Status", String.valueOf(isRunning));
    }

    @Override
    public void stopExecution() {
        if(isRunning) {
            handlerThread.quit();
            fetchBtnStatus.setToStart();
            isRunning = false;
        }
    }
}
