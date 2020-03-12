package com.example.coha.google.push;



/**
 * Created by Samchan on 2017-01-11.
 */


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.coha.google.MainBoardActivity;
import com.example.coha.google.MainBoardActivity;
import com.example.coha.google.R;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("fcm","1");
        sendNotification(remoteMessage.getData().get("message"));
    }

    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, MainBoardActivity.class);
        intent.putExtra("PushCode",messageBody);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
//                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("MyBestBoard에서 알림")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}

