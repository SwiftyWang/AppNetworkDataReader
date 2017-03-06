package com.swifty.datareader;

/**
 * Created by swifty on 3/3/2017.
 */

public interface IReader {
    /*
             *  Get network data received in bytes
             */
    long getReceivedData(int uid);

    /*
         *  Get network data transmitted in application
         */
    long getDataTransmitted(int uid);

    /*
         *  Get network packets received in application
         */
    long getPacketsReceived(int uid);

    /*
         *  Get network packets transmitted in application.
         */
    long getPacketsTransmitted(int uid);

    long getTotalReceived();

    long getTotalTransmitted();

    long getTotalPacketsReceived();

    long getTotalPacketsTransmitted();
}
