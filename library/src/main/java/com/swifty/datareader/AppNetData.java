package com.swifty.datareader;

/**
 * Created by swifty on 17/3/2017.
 */

public class AppNetData {
    public int uid;
    public long rx;
    public long tx;
    public long rp;
    public long tp;
    public String appName;
    public String packageName;


    public AppNetData(int uid, long rx, long tx, long rp, long tp, String appName, String packageName) {
        this.uid = uid;
        this.rx = rx;
        this.tx = tx;
        this.rp = rp;
        this.tp = tp;
        this.appName = appName;
        this.packageName = packageName;
    }

    public AppNetData(int uid, long rx, long tx, long rp, long tp) {
        this.uid = uid;
        this.rx = rx;
        this.tx = tx;
        this.rp = rp;
        this.tp = tp;

    }

    public AppNetData() {
    }
}
