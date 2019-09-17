package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aptitude.education.e2buddy.Intro.Helper;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Timer;
import java.util.TimerTask;

import static com.aptitude.education.e2buddy.Intro.Quizapp.CHANNEL_ID;

/**
 * Created by Matrix on 26-02-2019.
 */

public class FirebaseMessagingSerivce extends FirebaseMessagingService {


    DatabaseReference reference;
    LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String msgTitle = remoteMessage.getNotification().getTitle();
        String msgBody = remoteMessage.getNotification().getBody();
        String click_action =remoteMessage.getNotification().getClickAction();

        String sender_name = remoteMessage.getData().get("sender_name");
        String receiver_user_id = remoteMessage.getData().get("receiver_user_id");
        String notification_id = remoteMessage.getData().get("notification_id");
        String sender_id = remoteMessage.getData().get("sender_id");

        Log.e("deeke", "e2buddy");
        reference = FirebaseDatabase.getInstance().getReference();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(msgTitle)
                .setAutoCancel(true)
                .setContentText(msgBody)
                .setPriority(Notification.PRIORITY_HIGH);

        Intent intent = new Intent(click_action);

        intent.putExtra("sender_name", sender_name);
        intent.putExtra("receiver_user_id", receiver_user_id);
        intent.putExtra("notification_id", notification_id);
        intent.putExtra("sender_id", sender_id);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                                                            0,
                                                                        intent

                                                                        , PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        final int notification_id1 = (int) System.currentTimeMillis();

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(notification_id1,builder.build());

        long delayInMillis = 15000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                notificationManager.cancel(notification_id1);

            }
        }, delayInMillis);

        if (Helper.isAppRunning(getApplicationContext(), "com.aptitude.education.e2buddy")) {
            Intent intent1 = new Intent("NotificationData");
            intent1.putExtra("sender_name", remoteMessage.getData().get("sender_name"));
            intent1.putExtra("receiver_user_id", remoteMessage.getData().get("receiver_user_id"));
            intent1.putExtra("notification_id", remoteMessage.getData().get("notification_id"));
            intent1.putExtra("sender_id", remoteMessage.getData().get("sender_id"));
            localBroadcastManager.sendBroadcast(intent1);

            notificationManager.cancel(notification_id1);

        }

        reference.child("user_notifications").child(receiver_user_id).child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String cancel_status = dataSnapshot.child("cancel_status").getValue().toString();
                   // Toast.makeText(getApplicationContext(), ""+cancel_status,Toast.LENGTH_SHORT).show();

                    if (cancel_status.equals("yes")) {
                        notificationManager.cancel(notification_id1);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
