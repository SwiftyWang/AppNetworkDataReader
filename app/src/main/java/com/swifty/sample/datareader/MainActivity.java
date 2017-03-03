package com.swifty.sample.datareader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.swifty.datareader.AppDataReader;

import java.util.ArrayList;

/**
 * Created by swifty on 3/3/2017.
 */
public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    AppDataReader dataReader;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        update();

    }

    private void update() {
        if (AppDataReader.checkPermission(this)) {
            dataReader = new AppDataReader(this);
            ArrayList<ApplicationInfo> arrayList = (ArrayList<ApplicationInfo>) dataReader.getApplicationMeta();
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            recyclerViewAdapter = new RecyclerViewAdapter(this, arrayList);
            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(recyclerViewAdapter);
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
