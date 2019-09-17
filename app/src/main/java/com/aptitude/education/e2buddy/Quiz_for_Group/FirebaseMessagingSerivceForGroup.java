package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.aptitude.education.e2buddy.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Matrix on 26-02-2019.
 */

public class FirebaseMessagingSerivceForGroup extends FirebaseMessagingService {

    int min = 1;
    int max = 100;
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



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(msgTitle)
                .setAutoCancel(true)
                .setContentText(msgBody);

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

        int notification_id1 = (int) System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notification_id1,builder.build());


    }
}
