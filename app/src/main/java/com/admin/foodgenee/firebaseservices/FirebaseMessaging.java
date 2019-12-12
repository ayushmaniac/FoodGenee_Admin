package com.admin.foodgenee.firebaseservices;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.admin.foodgenee.R;
import com.admin.foodgenee.WebViewSite;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class FirebaseMessaging extends FirebaseMessagingService{


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent = new Intent(this, WebViewSite.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)

                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent)
        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000
        });

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setVibrationPattern(new long[]{
                    1000, 1000, 1000, 1000, 1000, 1000
            });
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_INSISTENT;
        manager.notify(0, notification);

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

    }
}
