package com.icandothisallday2020.ex63alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, new Date().toString(), Toast.LENGTH_SHORT).show();
        //다시 15초후에 알람이 발동하도록 재 알람 지정
        Intent intent2=new Intent(context,AlarmReceiver.class);
        PendingIntent pi=PendingIntent.
                getBroadcast(context,11,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+15000,pi);
        }else manager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+15000,pi);
    }
}
