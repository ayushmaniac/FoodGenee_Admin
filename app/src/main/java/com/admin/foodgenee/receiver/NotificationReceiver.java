package com.admin.foodgenee.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        playNotificationSound(context);

    }

    public void playNotificationSound(Context context) {
        try {
            Uri path = Uri.parse("android.resource://com.admin.foodgenee/raw/noti.mp3");
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, path);
            Log.i("TEST", "Ringtone Set to Resource: "+path.toString());
            RingtoneManager.getRingtone(context, path).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
