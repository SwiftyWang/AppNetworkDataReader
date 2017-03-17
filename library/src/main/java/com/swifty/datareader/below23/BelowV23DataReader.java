package com.swifty.datareader.below23;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.SparseArray;

import com.swifty.datareader.AppNetData;
import com.swifty.datareader.IReader;

import java.util.List;

/**
 * Created by Nikhil Bhutani on 7/13/2016.
 * <p/>
 * <p/>
 * Copyright 2016 Nikhil Bhutani
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class BelowV23DataReader implements IReader {

    public final Context context;

    public BelowV23DataReader(Context context) {
        this.context = context;
    }

    @Override
    public SparseArray<AppNetData> getAllAppData() {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        SparseArray<AppNetData> appNetDataSparseArray = new SparseArray<>();
        for (PackageInfo info : packages) {
            int uid = info.applicationInfo.uid;
            appNetDataSparseArray.put(uid, new AppNetData(uid, getDataReceived(uid), getDataTransmitted(uid), getPacketsReceived(uid), getDataTransmitted(uid), info.applicationInfo.name, info.packageName));
        }
        return appNetDataSparseArray;
    }

    /*
         *  Get network data received in bytes
         */
    @Override
    public long getDataReceived(int uid) {

        return TrafficStats.getUidRxBytes(uid);
    }


    /*
     *  Get network data transmitted in application
     */
    @Override
    public long getDataTransmitted(int uid) {

        return TrafficStats.getUidTxBytes(uid);

    }

    /*
     *  Get network packets received in application
     */
    @Override
    public long getPacketsReceived(int uid) {

        return TrafficStats.getUidRxPackets(uid);
    }

    /*
     *  Get network packets transmitted in application.
     */
    @Override
    public long getPacketsTransmitted(int uid) {

        return TrafficStats.getUidTxPackets(uid);
    }

    @Override
    public long getTotalReceived() {
        return TrafficStats.getMobileRxBytes();
    }

    @Override
    public long getTotalTransmitted() {
        return TrafficStats.getMobileTxBytes();
    }

    @Override
    public long getTotalPacketsReceived() {
        return TrafficStats.getMobileRxPackets();
    }

    @Override
    public long getTotalPacketsTransmitted() {
        return TrafficStats.getMobileTxPackets();
    }
}
