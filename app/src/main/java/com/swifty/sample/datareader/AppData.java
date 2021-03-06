package com.swifty.sample.datareader;

/**
 * Created by swifty on 6/3/2017.
 */

public class AppData {
    public int Uid;
    public String packageName;
    public String appName;
    public long received;
    public long transmitted;
    public long packageReceived;
    public long packageTransmitted;

    @Override
    public String toString() {
        return "AppData{" +
                "Uid=" + Uid +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", received=" + received +
                ", transmitted=" + transmitted +
                ", packageReceived=" + packageReceived +
                ", packageTransmitted=" + packageTransmitted +
                '}';
    }
}
