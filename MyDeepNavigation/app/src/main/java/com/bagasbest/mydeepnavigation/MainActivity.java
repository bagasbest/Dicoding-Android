package com.bagasbest.mydeepnavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpendetail = findViewById(R.id.btnOpenDetail);
        btnOpendetail.setOnClickListener(this);

        showNotification(MainActivity.this, getResources().getString(R.string.notification_title),
                getResources().getString(R.string.notification_message), 110);

    }

    private void showNotification(Context context, String title, String message, int i) {
       String CHANNEL_ID = "Channel_1";
       String CHANNEL_NAME = "Navigation channel";

       Intent notifDetailIntent = new Intent(this, DetailActivity.class);
       notifDetailIntent.putExtra(DetailActivity.EXTRA_TITLE, title);
       notifDetailIntent.putExtra(DetailActivity.EXTRA_MESSAGE, message);

        PendingIntent pendingIntent = TaskStackBuilder.create(this)
                .addParentStack(DetailActivity.class)
                .addNextIntent(notifDetailIntent)
                .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_email_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[] {1000,1000,1000,1000,1000})
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {1000,1000,1000,1000,1000});

            builder.setChannelId(CHANNEL_ID);

            if(notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if(notificationManager != null) {
            notificationManager.notify(i, notification);
        }



    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnOpenDetail){
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_TITLE, getString(R.string.detail_title));
            intent.putExtra(DetailActivity.EXTRA_MESSAGE, getString(R.string.detail_message));
            startActivity(intent);
        }
    }
}
