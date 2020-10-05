package com.mitul.wiss;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.RequiresApi;

public class ContinuousModeExecutor implements IModeExector{

    private HandlerThread handlerThread;
    private Handler handler;
    private AppFields appFields;
    private WifiManager wifiManager;

    private String ssid;
    private String mac;
    private String linkSpeed;
    private String frequency;
    private String bssid;
    private String signalScore;
    private String rssi;

    ContinuousModeExecutor(AppFields _appFields, WifiManager _wifiManager){
        appFields = _appFields;
        wifiManager = _wifiManager;
    }

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

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void execute() {
        handlerThread = new HandlerThread("ContUpdateWiSSThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        handler = new Handler(looper);

        fetchData();
        uploadToUI();

        handler.post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    fetchRssiOnly();
                    uploadRssiToUI();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void stopExecution() {
        handlerThread.quit();
    }
}
