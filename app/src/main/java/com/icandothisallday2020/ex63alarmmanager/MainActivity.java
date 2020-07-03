package com.icandothisallday2020.ex63alarmmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //알람매니저 객체 소환(:운영체제 내 존재)
        manager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }
    public void btn1(View view) {
        //10초후 실행할 Component(Activity,Service,BroadcastReceiver) 지정
        //알람에 설정할 Pending(보류)Intent 객체 생성
        Intent intent=new Intent(this, AlarmActivity.class);
        PendingIntent pi=PendingIntent.getActivity
                (this,13,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //ver.M(mashMellow)~ Doz(낮잠)모드가 있어서 깨우고 알람을 실행하도록
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            manager.setExactAndAllowWhileIdle
                    (AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+15000,pi);
            //현재시간을 milli second 로
            //Idle mode==Doz mode
        }else manager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+15000,pi);
    }
    public void btn2(View view) {
        //재알람 기능이 없어서 행운의 편지기법 사용
        //10초후에 실행할 작업
        Intent intent=new Intent(this,AlarmReceiver.class);
        PendingIntent pi=PendingIntent.
                getBroadcast(this,11,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,pi);
        }else manager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,pi);
    }

    public void btn3(View view) {
        if(manager!=null){
            Intent intent=new Intent(this, AlarmReceiver.class);
            PendingIntent pi=PendingIntent.getBroadcast(this,11,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            manager.cancel(pi);
        }
    }

    int Year,Month,Day,Hour,Min;

    public void btn4(View view) {
        //오늘 날짜&시간을 가진 객체 생성
        GregorianCalendar calendar=new GregorianCalendar();
        int y=calendar.get(Calendar.YEAR);//120:1900년 부터 계산
        int m=calendar.get(Calendar.MONTH);//6월==5:1월==0
        int d=calendar.get(Calendar.DAY_OF_MONTH);//8일 - 8
        //특정 날짜를 지정할 수 있도록 달력 다이얼로그 생성
        DatePickerDialog dialog=new DatePickerDialog(this,D_listener,y,m,d);
        // y,m,d 기준으로 선택한 날
        dialog.show();
    }
    //날짜선택한 것을 듣는 리스너
    DatePickerDialog.OnDateSetListener D_listener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //다이얼로그에서 날짜를 선택하면 선택된 값을 파라미터로 전달받음
            Year=year; Month=month; Day=dayOfMonth;

            //시간선택을 위한 다이얼로그
            GregorianCalendar calendar=new GregorianCalendar();
            int h=calendar.get(Calendar.HOUR_OF_DAY);
            int m=calendar.get(Calendar.MINUTE);
            TimePickerDialog dialog=new TimePickerDialog
                    (MainActivity.this,timeSetListener,h,m,true);
            dialog.show();

        }    };

    //시간 선택 리스너 객체 생성
    TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Hour=hourOfDay;//선택한 시간 받아오기
            Min=minute;
            //설정한 시간을 기준으로 알람지정
            GregorianCalendar calendar=new GregorianCalendar(Year,Month,Day,Hour,Min);
            Intent intent=new Intent(MainActivity.this,AlarmActivity.class);
            PendingIntent pi=PendingIntent.getActivity(MainActivity.this,
                    17,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pi);
            }else manager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pi);
        }
    };
}
