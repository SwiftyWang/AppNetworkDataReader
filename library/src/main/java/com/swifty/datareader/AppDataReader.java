package com.swifty.datareader;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.swifty.datareader.below23.BelowV21DataReader;
import com.swifty.datareader.v23.V23DataReader;

import java.util.List;

/**
 * Created by swifty on 3/3/2017.
 */

public class AppDataReader implements IReader {
    private static final String TAG = "AppDataReader";
    private final PackageManager packageManager;
    private IReader iReader;

    public AppDataReader(Context context) {
        packageManager = context.getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            iReader = new V23DataReader(context);
        } else {
            iReader = new BelowV21DataReader(context);
        }
    }

    public static boolean checkPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_PHONE_STATE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), context.getPackageName());
            if (mode != AppOpsManager.MODE_ALLOWED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public long getReceivedData(int uid) {
        return iReader.getReceivedData(uid);
    }

    @Override
    public long getDataTransmitted(int uid) {
        return iReader.getDataTransmitted(uid);
    }

    @Override
    public long getPacketsReceived(int uid) {
        return iReader.getPacketsReceived(uid);
    }

    @Override
    public long getPacketsTransmitted(int uid) {
        return iReader.getPacketsTransmitted(uid);
    }

    @Override
    public long getTotalReceived() {
        return iReader.getTotalReceived();
    }

    @Override
    public long getTotalTransmitted() {
        return iReader.getTotalTransmitted();
    }

    @Override
    public long getTotalPacketsReceived() {
        return iReader.getTotalPacketsReceived();
    }

    @Override
    public long getTotalPacketsTransmitted() {
        return iReader.getTotalPacketsTransmitted();
    }

    public List<ApplicationInfo> getApplicationMeta() {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    public CharSequence getAppName(ApplicationInfo myapplicationInfo) {
        return packageManager.getApplicationLabel(myapplicationInfo);
    }

    public Drawable getAppIcon(ApplicationInfo applicationInfo) {
        if (applicationInfo == null) return null;
        Drawable icon = packageManager.getApplicationIcon(applicationInfo);
        return icon;
    }

    public Drawable getAppIcon(String packageName) {
        Drawable icon = null;
        try {
            icon = packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.toString(), e);
        }
        return icon;
    }
}
