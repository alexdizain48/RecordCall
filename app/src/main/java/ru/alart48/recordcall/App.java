package ru.alart48.recordcall;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_RECORD = "ru.alart48.recordcall.CHANNEL_RECORD";

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_RECORD,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //channel.setDescription("CallIIIII");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
