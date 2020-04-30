package ru.alart48.recordcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_PERMISSION_CODE = 1000;
    PhoneStateReceiver mServiceReceiver;

    Context context;
    BroadcastReceiver updateUIReciver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPermissionFromDevice()) {

        } else {
            requestPermission();
            Toast.makeText(this, "Проверить настройки", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS
        }, REQUEST_PERMISSION_CODE);
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int read_phone_state_result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int read_contacts_result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED &&
                read_phone_state_result == PackageManager.PERMISSION_GRANTED &&
                read_contacts_result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0) {
                    StringBuilder messageBuilder = new StringBuilder(" ");
                    boolean permissionToStore = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToRecord = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToPhone = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToReadContacts = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Ок", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!permissionToStore) {
                            messageBuilder.append("  -сохранения записи на Ваше устройство\n\n");
                        }
                        if (!permissionToRecord) {
                            messageBuilder.append("   -возможности сделать запись\n\n");
                        }
                        if (!permissionToPhone) {
                            messageBuilder.append("   -определения начала звонка\n\n");
                        }
                        if (!permissionToReadContacts) {
                            messageBuilder.append("   -определения имя контакта, разговор с которым будет записан");
                        }
                        if (messageBuilder != null)
                            showPermissionDialog(this, messageBuilder);
                    }

                }
                break;
            }
        }
    }

    public void showPermissionDialog(Context context, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //String title = context.getResources().getString(R.string.app_name);
        String title = "Эти разрешения необходимы Для";
        builder.setTitle(title);
        builder.setMessage(message);

        String positiveText = "Ok";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        /*String negativeText = "Закрыть";
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });*/

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //@SuppressLint("BatteryLife")
    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        startActivityForResult(intent, REQUEST_PERMISSION_CODE);

        /*Intent intent = new Intent();
        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        startActivityForResult(intent, REQUEST_PERMISSION_CODE);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
