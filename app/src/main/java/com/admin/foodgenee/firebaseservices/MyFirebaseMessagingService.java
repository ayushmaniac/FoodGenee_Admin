package com.admin.foodgenee.firebaseservices;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private String TAG = "MyFirebaseMessagingService";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null) {
            Log.d("fcmdata", new Gson().toJson(remoteMessage.getData()));

                NotificationHelper.navigateNotification(getApplicationContext(), remoteMessage.getData().get("title"), remoteMessage.getData().get("content"));

        }
    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

}



