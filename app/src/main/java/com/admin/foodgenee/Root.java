package com.admin.foodgenee;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.fragments.dashboard.Dashboard;
import com.admin.foodgenee.fragments.orders.OrdersUI.Orders;
import com.admin.foodgenee.fragments.profile.NotificationActivity;
import com.admin.foodgenee.fragments.profile.Profile;
import com.admin.foodgenee.fragments.profile.profilemodel.NotificationModel;
import com.admin.foodgenee.loginmodel.LoginStatusModel;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import network.FoodGenee;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;

public class Root extends AppCompatActivity {

    String userId;
    SessionManager sessionManager;
    String firebaseToken;
    Switch turnOnOff;

    ActivityResultListener listener;
    String type="login";
    MediaPlayer mp;
    LabeledSwitch labeledSwitch;
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    ImageView notification;
    TextView tv_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        sessionManager = new SessionManager(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);
        labeledSwitch=findViewById(R.id.turnbutton);
        tv_count=findViewById(R.id.tv_count);
        notification=findViewById(R.id.notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Root.this, NotificationActivity.class);
                startActivity(i);
            }
        });
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                if (isOn) {
                    if(isNetworkAvailable())
                        throwCalls("1");
                    else Toast.makeText(Root.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
                } else {
                    if(isNetworkAvailable())
                        throwCalls("0");
                    else Toast.makeText(Root.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

                }
            }
        });
        mp=new MediaPlayer();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getFirebaseToken();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, "Food Q", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Food Q");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        type=getIntent().getStringExtra("type");
        if(type.equalsIgnoreCase("notification")){
            mp= MediaPlayer.create(getApplicationContext(),R.raw.noti);
            mp.start();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE );
            assert v != null;
            // Vibrate for 500 milliseconds
            if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O ) {
                v.vibrate(VibrationEffect. createOneShot ( 5000 ,
                        VibrationEffect. EFFECT_HEAVY_CLICK )) ;
            } else {
                //deprecated in API 26
                v.vibrate( 5000) ;
            }


        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Dashboard()).commit();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_dashboard:
                        selectedFragment = new Dashboard();
                        break;
                   /* case R.id.navigation_todaysorder:
                        selectedFragment = new TodaysOrde();
                        break;*/
                    case R.id.navigation_orders:
                        selectedFragment = new Orders();
                        break;
                    case R.id.navigation_profile:
                        selectedFragment = new Profile();
                        break;


                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem toggleservice = menu.findItem(R.id.toggleservice);
        labeledSwitch = (LabeledSwitch) toggleservice.getActionView();
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                if (isOn) {
                    if(isNetworkAvailable())
                        throwCalls("1");
                    else Toast.makeText(Root.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
//                    status.setText("SERVICE ON");

                } else {
                    if(isNetworkAvailable())
                        throwCalls("0");
                    else Toast.makeText(Root.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
//                    status.setText("SERVICE OFF");

                }
            }
        });

   *//*     turnOnOff.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if(isNetworkAvailable())
                    throwCalls("1");
                else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
//                    status.setText("SERVICE ON");

            } else {
                if(isNetworkAvailable())
                    throwCalls("0");
                else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
//                    status.setText("SERVICE OFF");

            }
        });*//*
//
        return true;
    }*/


    private void throwCalls(String s) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                     firebaseToken = task.getResult().getToken();
                    Log.e("firebaseToken",firebaseToken);
                });

        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<ResponseBody> call = foodGenee.sendStatus("login-status", userId,s, firebaseToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

//                startService(new Intent(WebViewSite.this,BroadcastService.class));

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(Root.this, "Some error occured", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getFirebaseToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    firebaseToken = task.getResult().getToken();


                });

    }

    private void validateStatus() {

        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<LoginStatusModel> call = foodGenee.checkLoginStatus("checkloginstatus", userId);
        call.enqueue(new Callback<LoginStatusModel>() {
            @Override
            public void onResponse(Call<LoginStatusModel> call, Response<LoginStatusModel> response) {

                if(response.body()!=null){
                    if(response.body().getLoginstatus()!=null){
                        if(response.body().getLoginstatus().equals("1")){
                            labeledSwitch.setOn(true);

                        } else labeledSwitch.setOn(false);
                    }

                }


            }

            @Override
            public void onFailure(Call<LoginStatusModel> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isNetworkAvailable()) {
            validateStatus();
            getNotificationList("notificationslist");
        }else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onResume() {
        super.onResume();
        //throwCalls("1");
        if(isNetworkAvailable()) {
            validateStatus();
            getNotificationList("notificationslist");

        } else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
    }


    private void getNotificationList(String type) {

        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<NotificationModel> call = foodGenee.getNotifications(type, userId);

        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                NotificationModel notificationModel=response.body();

                if(notificationModel!=null){
                    if(type.equalsIgnoreCase("notificationslist")){
                        if(notificationModel.getStatus().equalsIgnoreCase("1")){
                        tv_count.setText(notificationModel.getCount());


                        }else{
                            tv_count.setText("0");
                        }
                    }


                }else{
                    tv_count.setText("0");
                }


            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                tv_count.setText("0");
                Toast.makeText(Root.this, "Some error occured", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(listener != null){
            listener.myResult(requestCode, resultCode, data);
        }

    }


    public void setActivityResultListener(ActivityResultListener listener){
        this.listener = listener;
    }
    public void removeActivityResultListener(){
        listener = null;
    }

    public interface ActivityResultListener {
       void myResult(int requestCode, int resultCode, Intent data);
    }


    @Override
    protected void onDestroy() {
        mp.release();
        super.onDestroy();

    }
}

