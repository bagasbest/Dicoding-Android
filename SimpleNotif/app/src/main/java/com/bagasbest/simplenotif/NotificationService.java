package com.bagasbest.simplenotif;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import androidx.annotation.Nullable;


public class NotificationService extends IntentService {

    private static final String KEY_REPLY = "key_reply_message";
    private static String REPLY_ACTION = "com.bagasbest.simplenotif.REPLY_ACTION";
    public static CharSequence CHANNEL_NAME = "bagas channel";

    private int mNotificationId;
    private int mMessageId;

     NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
