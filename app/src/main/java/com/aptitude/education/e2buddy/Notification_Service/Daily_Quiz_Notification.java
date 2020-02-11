package com.aptitude.education.e2buddy.Notification_Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.aptitude.education.e2buddy.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.aptitude.education.e2buddy.Intro.Quizapp.CHANNEL_ID;

public class Daily_Quiz_Notification extends BroadcastReceiver {
    String currentDate;
    @Override
    public void onReceive(Context context, Intent intent) {

        RemoteViews notificationLayoutExpanded = new RemoteViews(context.getPackageName(), R.layout.custom_daily_quiz_notification);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = sdf.format(new Date());

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, StartQuizActivity.class);
        intent1.putExtra("quiz_date", currentDate);

        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);


        long when = System.currentTimeMillis();
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context,CHANNEL_ID)
                .setSmallIcon(R.drawable.applogo)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentTitle("E2Buddy")
                //.setContentText("Today Quiz is Live. Play to increase your Knowledge.")
                .setSound(sound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, mNotifyBuilder.build());


    }

}
