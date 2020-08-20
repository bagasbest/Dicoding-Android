package com.bagasbest.mysoundapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class MediaService extends Service implements MediaPlayerCallback{
   private MediaPlayer mediaPlayer = null;
   private boolean isReady;

    public final static String ACTION_CREATE = "com.bagasbest.mysoundapplication.mediaservice.create";
    public final static String ACTION_DESTROY = "com.bagasbest.mysoundapplication.mediaservice.destroy";
    final String TAG = MediaService.class.getSimpleName();

    public final static int PLAY = 0;
    public final static int STOP = 1;

    public MediaService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if(action != null) {
            switch (action) {
                case ACTION_CREATE:
                    if(mediaPlayer == null) {
                        init();
                    }
                    break;

                case ACTION_DESTROY:
                    if(!mediaPlayer.isPlaying()) {
                        stopSelf();
                    }
                    break;

                default:
                    break;
            }
        }
        Log.d(TAG, "onStartCommand: ");
        return flags;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mMessenger.getBinder();
    }


    @Override
    public void onPlay() {
        if(!isReady) {
            mediaPlayer.prepareAsync();
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    @Override
    public void onStop() {
        if(mediaPlayer.isPlaying() || isReady) {
            mediaPlayer.stop();
            isReady = false;
            stopNotif();
        }
    }

    private void init() {
        mediaPlayer = new MediaPlayer();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mediaPlayer.setAudioAttributes(attributes);
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.piece_of_cake);

        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isReady = true;
                mediaPlayer.start();
                showNotif();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }


    private final Messenger mMessenger = new Messenger(new IncomingHandler(this));

    static class IncomingHandler extends Handler {
        private WeakReference<MediaPlayerCallback> mediaPlayerCallbackWeakReference;

        IncomingHandler(MediaPlayerCallback playerCallback) {
            this.mediaPlayerCallbackWeakReference = new WeakReference<>(playerCallback);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case PLAY:
                    mediaPlayerCallbackWeakReference.get().onPlay();
                    break;

                case STOP:
                    mediaPlayerCallbackWeakReference.get().onStop();
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    void showNotif() {
        Intent notifIntent = new Intent(this, MainActivity.class);
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, 0 );

        String CHANNEL_DEFAULT_IMPORTANCE = "channel_test";
        int ONGOING_NOTIFICATION_ID = 1;

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle("Hey yo Notification Check!")
                .setContentText("Hey yo Bagas Ganteng Check!!!")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setTicker("Tesstoseron")
                .build();

        createChannel(CHANNEL_DEFAULT_IMPORTANCE);

        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    private void createChannel(String CHANNEL_ID) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Battrey", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(false);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
        }
    }

    void stopNotif() {
        stopForeground(false);
    }

}
