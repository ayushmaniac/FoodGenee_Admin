package com.admin.foodgenee.fragments.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.profile.profilemodel.NotificationModel;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.FoodGenee;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;

public class NotificationActivity extends AppCompatActivity {

    ImageView mIvBack;
    RecyclerView mRvNotifications;
    String userId;
    SessionManager sessionManager;
    LinearLayout mLLNotifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mIvBack=findViewById(R.id.iv_back);
        mLLNotifications=findViewById(R.id.ll_notifications);
        mRvNotifications=findViewById(R.id.rv_notifications);
        sessionManager = new SessionManager(this);
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        setSeenCount("seenstatus");
    }

    private void setSeenCount(String seenstatus) {
        getNotificationList(seenstatus);
    }

    private void getNotification(String type) {
        getNotificationList(type);
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

                            mRvNotifications.setVisibility(View.VISIBLE);
                            mLLNotifications.setVisibility(View.GONE);
                            mRvNotifications.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                            NotificationAdapter  notificationAdapter= new NotificationAdapter(notificationModel.getOrders(), NotificationActivity.this);
                            mRvNotifications.setAdapter(notificationAdapter);

                        }else{
                            mRvNotifications.setVisibility(View.GONE);
                            mLLNotifications.setVisibility(View.VISIBLE);
                        }
                    }else{
                        if(notificationModel.getStatus().equalsIgnoreCase("1")){
                            getNotification("notificationslist");
                        }


                    }


                }else{
                    mRvNotifications.setVisibility(View.GONE);
                    mLLNotifications.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                mRvNotifications.setVisibility(View.GONE);
                mLLNotifications.setVisibility(View.VISIBLE);
                Toast.makeText(NotificationActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
