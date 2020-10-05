package com.mitul.wiss;

import android.app.Activity;
import android.widget.TextView;

public class AppFields {

    public TextView ssid;
    public TextView mac;
    public TextView linkSpeed;
    public TextView frequency;
    public TextView bssid;
    public TextView signalScore;
    public TextView rssi;

    private Activity activity;

    public AppFields(Activity _activity){
        activity = _activity;
        ssid = activity.findViewById(R.id.ssid_text);
        mac = activity.findViewById(R.id.mac_text);
        linkSpeed = activity.findViewById(R.id.linkspeed_text);
        frequency = activity.findViewById(R.id.frequency_text);
        bssid = activity.findViewById(R.id.bssid_text);
        signalScore = activity.findViewById(R.id.score_text);
        rssi = activity.findViewById(R.id.rssi_text);
    }

    public void setUsingUiThread(final TextView textView, final String string){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(string);
            }
        });
    }

    public void setssid(String ssidText) {
        setUsingUiThread(ssid, ssidText);
    }

    public void setmac(String macText) {
        setUsingUiThread(mac, macText);
    }

    public void setlinkSpeed(String linkSpeedText) {
        setUsingUiThread(linkSpeed, linkSpeedText);
    }

    public void setfrequency(String frequencyText) {
        setUsingUiThread(frequency, frequencyText);
    }

    public void setBssid(String bssidText) {
        setUsingUiThread(bssid, bssidText);
    }

    public void setSignalScore(String signalScoreText) {
        setUsingUiThread(signalScore, signalScoreText);
    }

    public void setRssi(String rssiText) {
        setUsingUiThread(rssi, rssiText);
    }
}
