package com.example.exe12_2024;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
 boolean temp;
 int rqstcode;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences iset = context.getSharedPreferences("timeset", MODE_PRIVATE);
        temp = iset.getBoolean("settime", false);
        rqstcode = iset.getInt("rqstcode",0);
        if(temp){

        }
        else{
            SharedPreferences.Editor edit = iset.edit();
            edit.putInt("rqstcode", rqstcode++);
            edit.commit();
            MainActivity.getInstance().newalarminhour(rqstcode);

        }
    }
}