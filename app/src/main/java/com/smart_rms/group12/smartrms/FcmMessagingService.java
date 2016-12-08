package com.smart_rms.group12.smartrms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by savinda on 12/2/16.
 */

public class FcmMessagingService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String message  = remoteMessage.getNotification().getBody();

        if(title.equals("Order ready")){
            Intent intent= new Intent(this,UserArea.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuiler = new NotificationCompat.Builder(this);
            notificationBuiler.setContentTitle(title);
            notificationBuiler.setContentText(message);
            notificationBuiler.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuiler.setAutoCancel(true);
            notificationBuiler.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificationBuiler.build());
        }
        else if(title.equals("Waiter send you a message")){
            Intent intent= new Intent(this,PendingWork.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuiler = new NotificationCompat.Builder(this);
            notificationBuiler.setContentTitle(title);
            notificationBuiler.setContentText(message);
            notificationBuiler.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuiler.setAutoCancel(true);
            notificationBuiler.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificationBuiler.build());
        }
        else if(title.equals("")){

        }

    }
}
