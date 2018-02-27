package com.example.jesus.pksapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by jesus on 2/25/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        // to handle the notification
        // this action saved in thr JS file and notificationActivity manifest
        //that's mean when click in the notification it will throw to NotificationActivity
        String click_action = remoteMessage.getNotification().getClickAction();

        // this is to send title and body with the Intent to NotificationActivity
        //String dataMessage = remoteMessage.getData().get("message");
       // String dataFrom = remoteMessage.getData().get("from_user_id");

        // Build notification and send when the app in foreground not in background
        // because when the app in background the JS file is send the Notification without handling
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody);

        // move to click_action event (NotificationActivity) with the sender name and message body
        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("message",messageBody);
        resultIntent.putExtra("dataFrom",messageTitle);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId =(int) System.currentTimeMillis();
        NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId,mBuilder.build());
    }
}
