package com.swifty.datareader.below23;

import android.content.Context;
import android.net.TrafficStats;

import com.swifty.datareader.IReader;

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
public class BelowV21DataReader implements IReader {

    public final Context context;

    public BelowV21DataReader(Context context) {
        this.context = context;
    }

    /*
     *  Get network data received in bytes
     */
    @Override
    public long getReceivedData(int uid) {

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
        return TrafficStats.getTotalRxBytes();
    }

    @Override
    public long getTotalTransmitted() {
        return TrafficStats.getTotalTxBytes();
    }

    @Override
    public long getTotalPacketsReceived() {
        return TrafficStats.getTotalRxPackets();
    }

    @Override
    public long getTotalPacketsTransmitted() {
        return TrafficStats.getTotalTxPackets();
    }
}
