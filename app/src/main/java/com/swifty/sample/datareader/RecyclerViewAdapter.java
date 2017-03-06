package com.swifty.sample.datareader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swifty.datareader.AppDataReader;

import java.util.ArrayList;

/**
 * Created by swifty on 3/3/2017.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<ApplicationInfo> myDataList;
    private AppDataReader appDataReader;

    public RecyclerViewAdapter(Context context, ArrayList<ApplicationInfo> myDataList) {

        appDataReader = new AppDataReader(context);
        this.myDataList = myDataList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.icon.setImageDrawable(null);
            holder.appname.setText("total");
            holder.dataReceived.setText("Data Received :" + appDataReader.getTotalReceived());
            holder.dataTransmitted.setText("Data Transmitted :" + appDataReader.getTotalTransmitted());
            holder.packetsReceived.setText("Packets Received :" + appDataReader.getTotalPacketsReceived());
            holder.packetsTransmitted.setText("Packets Transmitted :" + appDataReader.getTotalPacketsTransmitted());
        } else {
            holder.icon.setImageDrawable(appDataReader.getAppIcon(myDataList.get(position)));
            holder.appname.setText(appDataReader.getAppName(myDataList.get(position)));
            holder.dataReceived.setText("Data Received :" + appDataReader.getReceivedData(myDataList.get(position + 1).uid));
            holder.dataTransmitted.setText("Data Transmitted :" + appDataReader.getDataTransmitted(myDataList.get(position + 1).uid));
            holder.packetsReceived.setText("Packets Received :" + appDataReader.getPacketsReceived(myDataList.get(position + 1).uid));
            holder.packetsTransmitted.setText("Packets Transmitted :" + appDataReader.getPacketsTransmitted(myDataList.get(position + 1).uid));
        }
    }


    @Override
    public int getItemCount() {
        return myDataList.size() + 1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView icon;
        final TextView appname;
        final TextView dataReceived;
        final TextView dataTransmitted;
        final TextView packetsReceived;
        final TextView packetsTransmitted;

        public ViewHolder(View itemView) {

            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.app_icon);
            appname = (TextView) itemView.findViewById(R.id.app_name);
            dataReceived = (TextView) itemView.findViewById(R.id.data_received);
            dataTransmitted = (TextView) itemView.findViewById(R.id.data_transmitted);
            packetsReceived = (TextView) itemView.findViewById(R.id.packets_received);
            packetsTransmitted = (TextView) itemView.findViewById(R.id.packets_transmitted);
        }
    }

}
