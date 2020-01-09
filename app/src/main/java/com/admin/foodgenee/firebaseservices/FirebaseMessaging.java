package com.admin.foodgenee.firebaseservices;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.admin.foodgenee.R;
import com.admin.foodgenee.Root;
import com.admin.foodgenee.WebViewSite;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class FirebaseMessaging extends FirebaseMessagingService{
    Dialog loadingDialog;


    public static String TAG  = "Messaging";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Intent intent = new Intent(this, Root.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Intent accept = new Intent(this, WebViewSite.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent acceptIntent = PendingIntent.getActivity(this, 0, accept, PendingIntent.FLAG_ONE_SHOT);

        Intent decline = new Intent(this, WebViewSite.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent declineIntent = PendingIntent.getActivity(this, 0, decline, PendingIntent.FLAG_ONE_SHOT);



        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)

                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.noti))
                .setVibrate(new long[]{10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000})
                .addAction(R.drawable.icon, getString(R.string.accept),
                        acceptIntent)
                .addAction(R.drawable.icon, getString(R.string.decline),
                        declineIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setVibrationPattern(new long[]{
                    10000, 10000, 10000, 10000, 10000, 10000, 10000
            });
            channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.noti), attributes);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_INSISTENT;
        manager.notify(0, notification);

        startAlarm();



    }




    private void startAlarm( ) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Root.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
    }



    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

    }

}
