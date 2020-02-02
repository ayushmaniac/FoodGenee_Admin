
package com.admin.foodgenee.firebaseservices;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.admin.foodgenee.R;
import com.admin.foodgenee.Root;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public final class NotificationHelper {

    public static final NotificationHelper INSTANCE;



    public static void navigateNotification(@NonNull Context context, @NonNull String title, @NonNull String body) {


        //please continue

        Intent intent = new Intent(context, Root.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type","notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, Root.NOTIFICATION_ID).setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentText(body)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title).setContentIntent(pendingIntent).setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(1, mBuilder.build());

        Intent i = new Intent(context, Root.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("type","notification");
        context.startActivity(i);

        //Done!
        //okay
        //is all done?
        //Yes everything is done, I'll just build the bundle and give it to either you or venu
        //okay thank you
        //great nice talking
        //same here :-)

    }


    private NotificationHelper() {
    }

    static {
        NotificationHelper var0 = new NotificationHelper();
        INSTANCE = var0;
    }
}
