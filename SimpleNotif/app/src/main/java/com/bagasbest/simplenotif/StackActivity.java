package com.bagasbest.simplenotif;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StackActivity extends AppCompatActivity {

    private EditText etSender;
    private EditText etMessage;

    private int idNotification = 0;
    private final List<NotificationItem> stackNotif = new ArrayList<>();

    private static final CharSequence CHANNEL_NAME = "bagas channel";
    private final static String GROUP_KEYS_EMAILS = "group_key_emails";
    private final static int NOTIFICATION_REQUEST_CODE = 200;
    private static final int MAX_NOTIFICATION = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);

        etSender = findViewById(R.id.etSender);
        etMessage = findViewById(R.id.etMessage);

        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sender = etSender.getText().toString().trim();
                String message = etMessage.getText().toString().trim();

                if(sender.isEmpty() || message.isEmpty()) {
                    Toast.makeText(StackActivity.this, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    stackNotif.add(new NotificationItem(idNotification, sender, message));
                    sendNotif();
                    idNotification++;
                    etSender.setText("");
                    etMessage.setText("");
                    InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }


            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        stackNotif.clear();
        idNotification = 0;
    }

    private void sendNotif() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeItem = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_notifications_none_24);
        Intent intent = new Intent(this, StackActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder;

        //melakukan pengecekan jika idNotification lebih kecil dari MAX NOTIF
        String CHANNEL_ID = "channel_01";
        if (idNotification < MAX_NOTIFICATION) {
            builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("New Email From: " + stackNotif.get(idNotification).getSender())
                    .setContentText(stackNotif.get(idNotification).getMessage())
                    .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                    .setLargeIcon(largeItem)
                    .setGroup(GROUP_KEYS_EMAILS)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine("New Email from: " + stackNotif.get(idNotification).getSender())
                    .addLine("New Email from " + stackNotif.get(idNotification - 1).getSender())
                    .addLine("New Email from " + stackNotif.get(idNotification - 2).getSender())
                    .setBigContentTitle(idNotification + " new emails")
                    .setSummaryText("mail@dicoding");
            builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(idNotification +  " new emails")
                    .setContentText("mail@dicoding.com")
                    .setSmallIcon(R.drawable.ic_baseline_mail_24)
                    .setGroup(GROUP_KEYS_EMAILS)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }

         /*
            Untuk android Oreo ke atas perlu menambahkan notification channel
            Materi ini akan dibahas lebih lanjut di modul extended
         */

         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
             /* Create or update. */
             NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                     CHANNEL_NAME,
                     NotificationManager.IMPORTANCE_DEFAULT);

             builder.setChannelId(CHANNEL_ID);

             if(mNotificationManager != null) {
                 mNotificationManager.createNotificationChannel(channel);
             }
         }

        Notification notification = builder.build();
         if(mNotificationManager != null) {
             mNotificationManager.notify(idNotification, notification);
         }
    }


}