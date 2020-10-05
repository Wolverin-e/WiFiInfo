package com.mitul.wiss;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class InstantaneousModeExecutor implements IModeExector {

    private AppFields appFields;
    private WifiManager wifiManager;

    private String ssid;
    private String mac;
    private String linkSpeed;
    private String frequency;
    private String bssid;
    private String signalScore;
    private String rssi;


    InstantaneousModeExecutor(AppFields _appFields, WifiManager _wifiManager){
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
        signalScore = String.valueOf("");
        Log.d("WifiInst", wifiInfo.toString());
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void execute(){
        fetchData();
        uploadToUI();
    }
}
