package com.swifty.datareader.v23;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.SparseArray;

import com.swifty.datareader.AppNetData;
import com.swifty.datareader.IReader;

/**
 * Created by swifty on 3/3/2017.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class V23DataReader implements IReader {

    private final Context context;
    NetworkStatsManager networkStatsManager;

    /**
     * init DataReader if the android verion is >= 23
     *
     * @param context
     */
    public V23DataReader(Context context) {
        this.context = context;
        networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
    }

    @Override
    public long getDataReceived(int uid) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis(),
                    uid);
        } catch (RemoteException e) {
            return -1;
        }
        long rxBytes = 0;
        while (networkStats.hasNextBucket()) {
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }

    @Override
    public long getDataTransmitted(int uid) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis(),
                    uid);
        } catch (RemoteException e) {
            return -1;
        }
        long txBytes = 0;
        while (networkStats.hasNextBucket()) {
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            txBytes += bucket.getTxBytes();
        }
        networkStats.close();
        return txBytes;
    }

    @Override
    public long getPacketsReceived(int uid) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis(),
                    uid);
        } catch (RemoteException e) {
            return -1;
        }
        long rxPackets = 0;
        while (networkStats.hasNextBucket()) {
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            networkStats.getNextBucket(bucket);
            rxPackets += bucket.getRxPackets();
        }
        networkStats.close();
        return rxPackets;
    }

    @Override
    public long getPacketsTransmitted(int uid) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis(),
                    uid);
        } catch (RemoteException e) {
            return -1;
        }
        long txPackets = 0;
        while (networkStats.hasNextBucket()) {
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            txPackets += bucket.getTxPackets();
        }
        networkStats.close();
        return txPackets;
    }

    @Override
    public long getTotalReceived() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        long rxBytes = bucket.getRxBytes();
        return rxBytes;
    }

    @Override
    public long getTotalTransmitted() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        long txBytes = bucket.getTxBytes();
        return txBytes;
    }

    @Override
    public long getTotalPacketsReceived() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        long rxPackets = bucket.getRxPackets();
        return rxPackets;
    }

    @Override
    public long getTotalPacketsTransmitted() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        long txPackets = bucket.getTxPackets();
        return txPackets;
    }

    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return "";
    }

    @Override
    public SparseArray<AppNetData> getAllAppData() {
        SparseArray<AppNetData> appNetDataMap = new SparseArray<>();
        NetworkStats networkStats;
        try {
            networkStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return appNetDataMap;
        }
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats != null && networkStats.hasNextBucket()) {
            boolean nextBucket = networkStats.getNextBucket(bucket);
            int absUid = Math.abs(bucket.getUid());
            if (nextBucket) {
                AppNetData appNetData = appNetDataMap.get(absUid);
                if (appNetData == null) {
                    appNetData = new AppNetData(absUid, bucket.getRxBytes(), bucket.getTxBytes(), bucket.getRxPackets(), bucket.getTxPackets(), getAppName(absUid), getPackageName(absUid));
                    appNetDataMap.put(absUid, appNetData);
                } else {
                    appNetData.rx += bucket.getRxBytes();
                    appNetData.tx += bucket.getTxBytes();
                    appNetData.rp += bucket.getRxPackets();
                    appNetData.tp += bucket.getTxPackets();
                }
            }
        }
        return appNetDataMap;
    }

    private String getPackageName(int absUid) {
        String[] packagesForUid = context.getPackageManager().getPackagesForUid(absUid);
        if (packagesForUid != null && packagesForUid.length > 1) return packagesForUid[0];
        else return null;
    }

    private String getAppName(int absUid) {
        return context.getPackageManager().getNameForUid(absUid);
    }
}
