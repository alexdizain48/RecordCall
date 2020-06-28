package ru.alart48.recordcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import ru.alart48.recordcall.services.RecordService;

public class PhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {  //входящийй
                Toast.makeText(context, "123", Toast.LENGTH_SHORT).show();
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {// on
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, RecordService.class));
                } else
                context.startService(new Intent(context, RecordService.class));
                Toast.makeText(context, "On", Toast.LENGTH_SHORT).show();

            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {// off
                context.stopService(new Intent(context, RecordService.class));
                Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
