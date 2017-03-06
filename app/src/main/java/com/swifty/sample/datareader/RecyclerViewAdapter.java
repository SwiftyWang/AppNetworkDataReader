package com.swifty.sample.datareader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swifty.datareader.AppDataReader;

import java.util.List;

/**
 * Created by swifty on 3/3/2017.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<AppData> myDataList;
    private AppDataReader appDataReader;

    public RecyclerViewAdapter(Context context, List<AppData> myDataList) {

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
        holder.icon.setImageDrawable(appDataReader.getAppIcon(myDataList.get(position).packageName));
        holder.appname.setText(myDataList.get(position).appName);
        holder.dataReceived.setText("Data Received :" + myDataList.get(position).received);
        holder.dataTransmitted.setText("Data Transmitted :" + myDataList.get(position).transmitted);
        holder.packetsReceived.setText("Packets Received :" + myDataList.get(position).packageReceived);
        holder.packetsTransmitted.setText("Packets Transmitted :" + myDataList.get(position).packageTransmitted);
    }


    @Override
    public int getItemCount() {
        return myDataList.size();
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
