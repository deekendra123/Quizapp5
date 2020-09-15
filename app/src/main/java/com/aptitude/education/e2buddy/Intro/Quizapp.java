package com.aptitude.education.e2buddy.Intro;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.aptitude.education.e2buddy.Notification_Service.Tournament_Quiz_Notification;
import com.aptitude.education.e2buddy.Notification_Service.Word_of_the_Day_Notification;
import com.aptitude.education.e2buddy.Notification_Service.Daily_Quiz_Notification;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Quizapp extends Application {

    public static final String CHANNEL_ID = "e2buddy";
    boolean connected = false;
    private int versionCode;
    private PackageInfo pInfo = null;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        createNotificationChannel();
        send_Daily_Quiz_Notification();
        send_Tournament_Quiz_Notification();
        sendUpdateNotification();


        send_Word_of_the_Day_Notification();
       /* CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();

        if (checkInternet.isOnline()){
            send_Word_of_the_Day_Notification();

        }
*/

     /*   ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
            Log.e("connection", String.valueOf(connected));
            send_Word_of_the_Day_Notification();

        }
        else {
            connected = false;
            Log.e("connection", String.valueOf(connected));

        }*/
    }

    private void sendUpdateNotification(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("updateVersion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   try {
                       versionCode = Integer.parseInt(dataSnapshot.child("versionCode").getValue().toString());
                    //   int versionName = Integer.parseInt(dataSnapshot.child("versionName").getValue().toString());
                       pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                int currentAppVersionCode = pInfo.versionCode;
                if(versionCode>currentAppVersionCode){

                    RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.custom_update_notification);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.aptitude.education.e2buddy"));
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

                    long when = System.currentTimeMillis();
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                            getApplicationContext(),CHANNEL_ID)
                            .setSmallIcon(R.drawable.applogo)
                            .setCustomBigContentView(notificationLayoutExpanded)
                            .setContentTitle("E2Buddy")
                            .setSound(sound)
                            .setAutoCancel(true).setWhen(when)
                            .setContentIntent(pendingIntent);

                    notificationManager.notify(0, mNotifyBuilder.build());


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        calendar.set(Calendar.HOUR_OF_DAY,18);
        calendar.set(Calendar.MINUTE,11);
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
