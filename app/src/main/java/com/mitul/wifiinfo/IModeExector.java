package com.mitul.wifiinfo;

public interface IModeExector {

    final int max_rssi_dbm = 200;

    public void execute();
    public void stopExecution();
}
