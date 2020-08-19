package com.bagasbest.simplenotif;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static com.bagasbest.simplenotif.NotificationService.CHANNEL_ID;
import static com.bagasbest.simplenotif.NotificationService.CHANNEL_NAME;
import static com.bagasbest.simplenotif.NotificationService.REPLY_ACTION;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private static final String KEY_NOTIFICATION_ID = "key_notification_id";
    private static String KET_MESSAGE_ID = "key_message_id";

    public static Intent getReplyMessageIntent (Context context, int notificationId, int messageId) {
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.setAction(REPLY_ACTION);
        intent.putExtra(KEY_NOTIFICATION_ID, notificationId);
        intent.putExtra(KET_MESSAGE_ID, messageId);
        return intent;
    }

    public NotificationBroadcastReceiver(){
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        CharSequence message = NotificationService.getReplyMessage(intent);
        int messageId = intent.getIntExtra(KET_MESSAGE_ID, 0);

        Toast.makeText(context, "Message ID: " + messageId + "\nMessage: " + message, Toast.LENGTH_SHORT).show();

        int notifyId = intent.getIntExtra(KEY_NOTIFICATION_ID, 1);
        updateNotification(context, notifyId);
    }

    private void updateNotification(Context context, int notifyId) {
        NotificationManager mNoftificationManaget = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                .setContentTitle(context.getString(R.string.notif_title_sent))
                .setContentText(context.getString(R.string.notif_content_sent));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            builder.setChannelId(CHANNEL_ID);

            if(mNoftificationManaget != null) {
                mNoftificationManaget.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if(mNoftificationManaget != null) {
            mNoftificationManaget.notify(notifyId, notification);
        }
    }

}
