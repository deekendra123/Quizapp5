package com.aptitude.education.e2buddy.Notification_Service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.aptitude.education.e2buddy.Intro.Quizapp.CHANNEL_ID;

public class Word_of_the_Day_Notification extends BroadcastReceiver {

    private String systemdate, imageUrl;
    private DatabaseReference databaseReference;
    private NotificationTarget notificationTarget;
    private CheckInternet checkInternet;


    @Override
    public void onReceive(Context context, Intent intent) {

        checkInternet = new CheckInternet(context);
      //  checkInternet.checkConnection();

        if (checkInternet.isOnline()){

            RemoteViews notificationLayoutExpanded= new RemoteViews(context.getPackageName(), R.layout.custom_wordoftheday_notification);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
            systemdate = sdf.format(new Date());
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("word_of_the_month").child(systemdate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        imageUrl = dataSnapshot1.child("image").getValue(String.class);
                    }

                    Intent intent1 = new Intent(context, HomeNevActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);


                    long when = System.currentTimeMillis();
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                            context,CHANNEL_ID)
                            .setSmallIcon(R.drawable.applogo)
                            .setContentTitle("Word of the Day")
                            .setContent(notificationLayoutExpanded)
                            .setPriority( NotificationCompat.PRIORITY_MIN)
                            .setAutoCancel(true)
                            .setWhen(when)
                            .setContentIntent(pendingIntent);

                    final Notification notification = mNotifyBuilder.build();

                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        notification.bigContentView = notificationLayoutExpanded;
                    }

                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, notification);

                    notificationTarget = new NotificationTarget(
                            context,
                            R.id.logo,
                            notificationLayoutExpanded,
                            notification,
                            0);

                    Glide
                            .with(context.getApplicationContext())
                            .asBitmap()
                            .load(imageUrl)

                            .into(notificationTarget);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

}

