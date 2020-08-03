package com.bagasbest.myworkmanager.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.bagasbest.myworkmanager.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MyWorker extends Worker {


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private static final String TAG = MyWorker.class.getSimpleName();
    private static final String APP_ID = "b16627615e4febf5dc5013dfc7cf9d8b";
    public static final String EXTRA_CITY = "city";
    private Result resultStatus;


    @NonNull
    @Override
    public Result doWork() {
        String dataCiity = getInputData().getString(EXTRA_CITY);
        Result status = getCurrentWeather(dataCiity);
        return status;
    }

    private Result getCurrentWeather(final String city) {
        Log.d(TAG, "getCurrentWeather: Mulai......");
        SyncHttpClient client = new SyncHttpClient();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city+ "&appid=" + APP_ID;
        Log.d(TAG, "getCurrentWeather: " + url);
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);

                try {
                    JSONObject responseObject = new JSONObject(result);

                    String currentWeather = responseObject.getJSONArray("weather")
                            .getJSONObject(0).getString("main");
                    String description  = responseObject.getJSONArray("weather")
                            .getJSONObject(0).getString("description");

                    double tempInKelvin = responseObject.getJSONObject("main").getDouble("temp");
                    double tempInCelcius = tempInKelvin - 273;

                    String temperature = new DecimalFormat("##.##").format(tempInCelcius);

                    String title = "Current weather in " +city;
                    String message = currentWeather + ", " + description + " with " + temperature + " celcius";

                    showNotification(title, message);

                    Log.d(TAG, "onSuccess: selesai.....");
                    resultStatus = Result.success();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    showNotification("Get current weather failed", error.getMessage());
                    Log.d(TAG,"onFailure:gagal....");
                    resultStatus = Result.failure();
            }
        });
        return resultStatus;
    }

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_1";
    private static final String CHANNEL_NAME = "bagas_channel";

    private void showNotification(String title, String message) {

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_repeat_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            builder.setChannelId(CHANNEL_ID);
            if(notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if(notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }


    }
}
