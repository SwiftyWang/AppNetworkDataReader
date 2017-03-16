package com.swifty.sample.datareader;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.usage.NetworkStats;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.swifty.datareader.AppDataReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by swifty on 3/3/2017.
 */
public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView recyclerView;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        update();

    }

    private void update() {
        if (AppDataReader.checkPermission(this)) {
            Observable.fromCallable(new Callable<List<AppData>>() {
                @Override
                public List<AppData> call() throws Exception {
                    AppDataReader dataReader = new AppDataReader(MainActivity.this);
                    List<AppData> appDatas = new ArrayList<>();
                    AppData total = new AppData();
                    total.appName = "total";
                    total.received = dataReader.getTotalReceived();
                    total.transmitted = dataReader.getTotalTransmitted();
                    total.packageReceived = dataReader.getTotalPacketsReceived();
                    total.packageTransmitted = dataReader.getTotalPacketsTransmitted();
                    appDatas.add(total);
                    Log.d(TAG, total.toString());

                    //add removed data usage
                    AppData removedData = new AppData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        removedData.Uid = NetworkStats.Bucket.UID_REMOVED;
                    } else {
                        removedData.Uid = -4;
                    }
                    removedData.appName = "removed";
                    removedData.received = dataReader.getReceivedData(removedData.Uid);
                    removedData.transmitted = dataReader.getDataTransmitted(removedData.Uid);
                    removedData.packageReceived = dataReader.getPacketsReceived(removedData.Uid);
                    removedData.packageTransmitted = dataReader.getPacketsTransmitted(removedData.Uid);
                    appDatas.add(removedData);
                    Log.d(TAG, removedData.toString());


                    //add tethering data usage
                    AppData tetheringData = new AppData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tetheringData.Uid = NetworkStats.Bucket.UID_TETHERING;
                    } else {
                        tetheringData.Uid = -5;
                    }
                    tetheringData.appName = "tethering";
                    tetheringData.received = dataReader.getReceivedData(tetheringData.Uid);
                    tetheringData.transmitted = dataReader.getDataTransmitted(tetheringData.Uid);
                    tetheringData.packageReceived = dataReader.getPacketsReceived(tetheringData.Uid);
                    tetheringData.packageTransmitted = dataReader.getPacketsTransmitted(tetheringData.Uid);
                    appDatas.add(tetheringData);
                    Log.d(TAG, tetheringData.toString());

                    ArrayList<ApplicationInfo> arrayList = (ArrayList<ApplicationInfo>) dataReader.getApplicationMeta();
                    for (ApplicationInfo applicationInfo : arrayList) {
                        AppData appData = new AppData();
                        appData.Uid = applicationInfo.uid;
                        appData.appName = dataReader.getAppName(applicationInfo).toString();
                        appData.packageName = applicationInfo.packageName;
                        appData.received = dataReader.getReceivedData(applicationInfo.uid);
                        appData.transmitted = dataReader.getDataTransmitted(applicationInfo.uid);
                        appData.packageReceived = dataReader.getPacketsReceived(applicationInfo.uid);
                        appData.packageTransmitted = dataReader.getPacketsTransmitted(applicationInfo.uid);
                        appDatas.add(appData);
                        Log.d(TAG, appData.toString());
                    }
                    return appDatas;
                }
            })
                    .map(new Function<List<AppData>, List<AppData>>() {
                        @Override
                        public List<AppData> apply(@io.reactivex.annotations.NonNull List<AppData> appDatas) throws Exception {
                            Collections.sort(appDatas, new Comparator<AppData>() {
                                @Override
                                public int compare(AppData appData, AppData t1) {
                                    if (appData.received < t1.received) {
                                        return 1;
                                    } else if (appData.received > t1.received) {
                                        return -1;
                                    } else {
                                        return 0;
                                    }
                                }
                            });
                            return appDatas;
                        }
                    })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<AppData>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            progress = ProgressDialog.show(MainActivity.this, null, "loading data");
                            Log.e(MainActivity.class.getSimpleName(), "onSubscribe");
                        }

                        @Override
                        public void onNext(List<AppData> appDatas) {
                            progress.dismiss();
                            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, appDatas);
                            recyclerView.setAdapter(recyclerViewAdapter);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(MainActivity.class.getSimpleName(), e.toString(), e);
                        }

                        @Override
                        public void onComplete() {
                            Log.e(MainActivity.class.getSimpleName(), "onComplete");
                        }
                    });
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            Toast.makeText(this, getText(com.swifty.datareader.R.string.please_grant_permission), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    update();
                } else {
                    finish();
                }
            }
        }
    }
}
