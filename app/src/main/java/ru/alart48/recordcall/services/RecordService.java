package ru.alart48.recordcall.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.alart48.recordcall.MainActivity;
import ru.alart48.recordcall.R;

import static ru.alart48.recordcall.App.CHANNEL_RECORD;

public class RecordService extends Service {

    private static final int NOTIFY_ID = 101;
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    int BufferElements2Rec = 1024;
    int BytesPerElement = 2;

    private MediaPlayer player;
    private AudioRecord audioRecorder;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;

    private NotificationManagerCompat notificationManager;

  /*  public RecordService() {
    }*/

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
        Log.d("TAG", "onStartCommand: ");

        notificationManager = NotificationManagerCompat.from(this);


            Notification notification = new NotificationCompat.Builder(this, CHANNEL_RECORD)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Calling Running")
                    .setContentText("Record IIIIIII")
                    .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFY_ID, notification);
        } else {
            notificationManager.notify(NOTIFY_ID, notification);
        }

        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        startRecord(mediaProjection);

       /* Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        player = new MediaPlayer();
        try {
            player.setDataSource(getApplicationContext(), soundUri);
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
        player.start();*/

        return START_NOT_STICKY;
    }

    private void startRecord(MediaProjection mediaProjection) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            AudioPlaybackCaptureConfiguration config = new AudioPlaybackCaptureConfiguration.Builder(mediaProjection)
                    .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
            AudioFormat audioFormat = new AudioFormat.Builder()
                    .setEncoding(RECORDER_AUDIO_ENCODING)
                    .setSampleRate(RECORDER_SAMPLERATE)
                    .setChannelMask(RECORDER_CHANNELS)
                    .build();
            audioRecorder = new AudioRecord.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setAudioFormat(audioFormat)
                    .setBufferSizeInBytes(BufferElements2Rec * BytesPerElement)
                    .setAudioPlaybackCaptureConfig(config)
                    .build();

            audioRecorder.startRecording();
            writeAudioDataToFile();
        }
    }

    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        File sampleDir = new File(getExternalFilesDir(null), "/RecotdDir");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        String fileName = "Record-" + new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss")
                .format(new Date()) + ".pcm";
        String filePath = sampleDir.getAbsolutePath() + "/" + fileName;

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*while (isRecording) {
            // gets the voice output from microphone to byte format
            audioRecorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Short wirting to file" + sData.toString());
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        notificationManager.cancel(NOTIFY_ID);

        player.stop();
    }
}
