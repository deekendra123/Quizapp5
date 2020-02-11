package com.aptitude.education.e2buddy.Intro;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.aptitude.education.e2buddy.Notification_Service.Tournament_Quiz_Notification;
import com.aptitude.education.e2buddy.Notification_Service.Word_of_the_Day_Notification;
import com.aptitude.education.e2buddy.Notification_Service.Daily_Quiz_Notification;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class Quizapp extends Application {

    public static final String CHANNEL_ID = "e2buddy";
    boolean connected = false;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        createNotificationChannel();
        send_Daily_Quiz_Notification();
        send_Tournament_Quiz_Notification();

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
            Log.e("connection", String.valueOf(connected));
            send_Word_of_the_Day_Notification();

        }
        else {
            connected = false;
            Log.e("connection", String.valueOf(connected));

        }
    }

    private void send_Tournament_Quiz_Notification(){

        Calendar cal1 = Calendar.getInstance();
        if (cal1.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY || cal1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,13);
            calendar.set(Calendar.MINUTE,00);
            calendar.set(Calendar.SECOND,01);
            if(Calendar.getInstance().after(calendar)){
                calendar.add(Calendar.DATE,1);
            }
            Intent intent = new Intent(getApplicationContext(), Tournament_Quiz_Notification.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
        }
    }


    private void send_Daily_Quiz_Notification(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,01);
        if(Calendar.getInstance().after(calendar)){
            calendar.add(Calendar.DATE,1);
        }
        Intent intent = new Intent(getApplicationContext(), Daily_Quiz_Notification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void send_Word_of_the_Day_Notification(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,01);
        if(Calendar.getInstance().after(calendar)){
            calendar.add(Calendar.DATE,1);
        }
        Intent intent = new Intent(getApplicationContext(), Word_of_the_Day_Notification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "E2-buddy",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            final NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }



}
