package com.example.exe12_2024;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static MainActivity ins;
    boolean temp;
    int rqstcode;
    PendingIntent alarmIntent;
    AlarmManager alarmMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ins = this;
    }


    public void settime(View view) {
        SharedPreferences iset = getSharedPreferences("timeset", MODE_PRIVATE);
        rqstcode = iset.getInt("rqstcode",0);
        SharedPreferences.Editor edit = iset.edit();
        edit.putBoolean("settime",true);
        openTimePickerDialog(true);

    }



    /**
     * openTimePickerDialog method
     * <p> Open dialog to use to pick time of day
     * </p>
     *
     * @param is24r boolean indicating whether time picker dialog for 24-hour
     */
    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Choose time");
        timePickerDialog.show();
    }
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        /**
         * onTimeSet method
         * <p> Return the time of day picked by the user
         * </p>
         *
         * @param view the time picker view that triggered the method
         * @param hourOfDay the hour the user picked
         * @param minute the minute the user picked
         */
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar calSet) {
        rqstcode++;
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("msg",String.valueOf(rqstcode)+" TOD");
        alarmIntent = PendingIntent.getBroadcast(this,
                rqstcode, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                calSet.getTimeInMillis(), alarmIntent);
        Toast.makeText(ins, (String.valueOf(rqstcode)+" Alarm in "+String.valueOf(calSet.getTime())), Toast.LENGTH_SHORT).show();
    }

    public void out(View view) {finish();}

    public static MainActivity  getInstance(){
        return ins;
    }

    public void newalarminhour(int t) {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(ins, AlarmReceiver.class);
                intent.putExtra("msg",String.valueOf(t)+" 1 hour");
                alarmIntent = PendingIntent.getBroadcast(ins,
                        t, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 1000*3600, alarmIntent);
            }
        });
    }
}