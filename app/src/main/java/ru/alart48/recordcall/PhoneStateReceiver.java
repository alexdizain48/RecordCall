package ru.alart48.recordcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import ru.alart48.recordcall.services.AudioPlayer;

public class PhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {  //входящийй
                Toast.makeText(context, "123", Toast.LENGTH_SHORT).show();
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {// on
                context.startService(new Intent(context, AudioPlayer.class));
                Toast.makeText(context, "On", Toast.LENGTH_SHORT).show();

            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {// off
                context.stopService(new Intent(context, AudioPlayer.class));
                Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
