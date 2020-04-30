package ru.alart48.recordcall.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

public class AudioPlayer extends Service {

    private MediaPlayer player;
    Context context;
    BroadcastReceiver updateUIReciver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //String tt = "/mnt/sdcard/Download/speech.ogg";
        final String DATA_SD = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/speech.ogg";
        player = new MediaPlayer();
        try {
            player.setDataSource(DATA_SD);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setLooping(true);
        player.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        player.stop();
    }
}
