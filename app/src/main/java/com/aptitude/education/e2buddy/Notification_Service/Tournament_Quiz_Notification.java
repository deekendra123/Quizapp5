package com.aptitude.education.e2buddy.Notification_Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;


import static com.aptitude.education.e2buddy.Intro.Quizapp.CHANNEL_ID;

public class Tournament_Quiz_Notification extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        RemoteViews notificationLayoutExpanded = new RemoteViews(context.getPackageName(), R.layout.custom_tournament_notification);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, HomeNevActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        long when = System.currentTimeMillis();
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context,CHANNEL_ID)
                .setSmallIcon(R.drawable.applogo)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentTitle("E2Buddy")
                .setSound(sound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, mNotifyBuilder.build());


    }

}
