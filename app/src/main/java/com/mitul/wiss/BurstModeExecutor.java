package com.mitul.wiss;

import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class BurstModeExecutor implements IModeExector {

    private AppFields appFields;
    private WifiManager wifiManager;
    private CountDownTimer countDownTimer;

    private boolean isRunning;

    private String ssid;
    private String mac;
    private String linkSpeed;
    private String frequency;
    private String bssid;
    private String signalScore;
    private String rssi;

    BurstModeExecutor(AppFields _appFields, WifiManager _wifiManager){
        appFields = _appFields;
        wifiManager = _wifiManager;
        isRunning = false;
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
        rssi = String.valueOf(wifiInfo.getRssi())+"dBm";
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
        rssi = String.valueOf(wifiInfo.getRssi())+"dBm";
        double score = (wifiInfo.getRssi()+127)*100/(max_rssi_dbm+127);
        signalScore = String.valueOf(Math.round(score))+"%";
    }

    private void uploadRssiToUI(){
        appFields.setSignalScore(signalScore);
        appFields.setRssi(rssi);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void execute() {
        if(isRunning){
            stopExecution();
        } else {
            isRunning = true;

            fetchData();
            uploadToUI();

            final List<Integer> rssis = new ArrayList<Integer>();

            countDownTimer = new CountDownTimer(5000, 500){
                @Override
                public void onTick(long millisUntilFinished) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    rssis.add(wifiInfo.getRssi());
                    Log.i("BurstExecLoop:", String.valueOf(wifiInfo.getRssi()));
                }

                @Override
                public void onFinish() {
                    double avgRssi = 0;
                    for (int x: rssis){
                        avgRssi += x;
                    }
                    avgRssi /= rssis.size();

                    double score = (avgRssi+127)*100/(max_rssi_dbm+127);

                    rssi = String.valueOf(Math.round(avgRssi))+" dBm";
                    signalScore = String.valueOf(Math.round(score))+"%";
                    appFields.setRssi(rssi);
                    appFields.setSignalScore(signalScore);
                    Log.i("BurstExecLoop:", rssi+signalScore);
                }
            };

            countDownTimer.start();
        }

        Log.i("ContExecution Status", String.valueOf(isRunning));
    }

    @Override
    public void stopExecution() {
        isRunning = false;
        countDownTimer.cancel();
    }
}
