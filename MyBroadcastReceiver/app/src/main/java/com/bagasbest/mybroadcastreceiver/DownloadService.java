package com.bagasbest.mybroadcastreceiver;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;


public class DownloadService extends IntentService {

    public static final String TAG = "DownloadService";
    public DownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Download Service dijalankan");
        if(intent != null) {
            try {
                Thread.sleep(3000);
            }catch (Exception e) {
                e.printStackTrace();
            }

            Intent notifyFinishIntent = new Intent(MainActivity.ACTION_DOWNLOAD_STATUS);
            sendBroadcast(notifyFinishIntent);
        }
    }
}
