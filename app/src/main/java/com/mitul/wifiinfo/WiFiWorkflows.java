package com.mitul.wifiinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class WiFiWorkflows {

    private WifiManager wifiManager;
    private Context context;
    private AlertDialog wifiNotConnectedDialogue;

    WiFiWorkflows(WifiManager _wifiManager, Context _context){
        wifiManager = _wifiManager;
        context = _context;
        buildDialogue();
    }

    private void buildDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("WiFiErr")
                .setMessage("Please Connect To a WiFi Network")
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.R)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
        wifiNotConnectedDialogue = builder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public boolean isWiFiConnected(){
        return (wifiManager.getConnectionInfo().getSSID() != WifiManager.UNKNOWN_SSID);
    }

    public void showWiFiNotConnected(){
        wifiNotConnectedDialogue.show();
    }
}
