package com.bagasbest.mysoundapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSound;

    SoundPool sp;
    int soundId;
    boolean spLoad = false;

    private MediaPlayer mediaPlayer = null;
    private boolean isReady;
    private Button btnPlay, btnStop;

    private final String TAG = MainActivity.class.getSimpleName();
    private Messenger mService = null;
    private Intent mBoundServiceIntent;
    private boolean mServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSound = findViewById(R.id.btnSound);
        btnPlay = findViewById(R.id.btn_play);
        btnStop = findViewById(R.id.btn_stop);

        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);





        sp = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(status == 0) {
                    spLoad = true;
                } else {
                    Toast.makeText(MainActivity.this, "Gagal Load", Toast.LENGTH_SHORT).show();
                }
            }
        });

        soundId = sp.load(this,R.raw.percussion_sound, 1);

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spLoad) {
                    sp.play(soundId, 1,1,0,0,1);
                }
            }
        });

        mBoundServiceIntent = new Intent(MainActivity.this, MediaService.class);
        mBoundServiceIntent.setAction(MediaService.ACTION_CREATE);

        startService(mBoundServiceIntent);
        bindService(mBoundServiceIntent, mSeviceConnection, BIND_AUTO_CREATE);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_play :
                if(!mServiceBound) return;
                try {
                    mService.send(Message.obtain(null, MediaService.PLAY, 0, 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_stop :
                if(!mServiceBound) return;
                try {
                    mService.send(Message.obtain(null, MediaService.STOP, 0, 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    private ServiceConnection mSeviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mServiceBound =false;
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        unbindService(mSeviceConnection);
        mBoundServiceIntent.setAction(MediaService.ACTION_DESTROY);

        startService(mBoundServiceIntent);
    }
}