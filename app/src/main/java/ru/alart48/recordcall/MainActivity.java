package ru.alart48.recordcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkPermissionFromDevice()) {
            Toast.makeText(this, "Можно работать", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 456);*/
        } else {
            requestPermission();
            Toast.makeText(this, "Проверить настройки", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_STATE
        }, REQUEST_PERMISSION_CODE);
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int read_phone_state_result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        return  write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED &&
                read_phone_state_result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                /*if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
            }*/
                if (grantResults.length > 0) {
                    String messageToStore = "";
                    String messageToRecord = "";
                    StringBuilder messageBuilder = new StringBuilder(" ");
                    boolean permissionToStore = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToRecord = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToPhone = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Ок", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!permissionToStore) {
                           // messageToStore = "Для сохранения записи необходим доступ к файлам";
                            messageBuilder.append("%[3] Для сохранения записи необходим доступ к файлам \n");
                        }
                        if (!permissionToRecord) {
                            messageToRecord = "Для возможности сделать запись";
                            messageBuilder.append(messageToRecord);
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
        String title = "Требуются разрешения";
        builder.setTitle(title);
        builder.setMessage(message);

        String positiveText = "Настройки";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openAppSettings();
            }
        });
        String negativeText = "Закрыть";
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

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


    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }*/

    @Override
    protected void onStop() {
        super.onStop();
    }

}
