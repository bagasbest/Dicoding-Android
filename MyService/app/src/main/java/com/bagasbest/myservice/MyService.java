package com.bagasbest.myservice;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.lang.ref.WeakReference;

import static android.content.ContentValues.TAG;

public class MyService extends Service implements DummyAsyncCallback {
    public static  final String TAG = "MyService";
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service dijalankan... ");

        //langkah 4 : init dan jalankan AsyncTask
        DummyAsync  dummyAsync = new DummyAsync(this);
        dummyAsync.execute();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    //lanngkah 5 : tmbahkan aksi di callback
    @Override
    public void preAsync() {
        Log.d(TAG, "preAsync: Mulai......");
    }

    @Override
    public void postAsync() {
        Log.d(TAG, "postAsync: Selesai......");
        stopSelf();
    }

    //langkah 3 : buat AsyncTask dan WeakReference
    private static class DummyAsync extends AsyncTask<Void, Void, Void> {

        private final WeakReference<DummyAsyncCallback> callback;

        private DummyAsync(DummyAsyncCallback callback) {
            this.callback = new WeakReference<>(callback);
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
            callback.get().preAsync();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: ");
            try {
                Thread.sleep(3000);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExexute: ");
            callback.get().postAsync();
        }
    }
}
