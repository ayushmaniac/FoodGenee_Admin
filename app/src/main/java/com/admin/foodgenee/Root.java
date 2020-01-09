package com.admin.foodgenee;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.admin.foodgenee.fragments.dashboard.Dashboard;
import com.admin.foodgenee.fragments.orders.OrdersUI.Orders;
import com.admin.foodgenee.fragments.profile.Profile;
import com.admin.foodgenee.fragments.todaysorder.TodaysOrde;
import com.admin.foodgenee.loginmodel.LoginStatusModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.switch_layout);
        sessionManager = new SessionManager(this);
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getFirebaseToken();

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
                    case R.id.navigation_todaysorder:
                        selectedFragment = new TodaysOrde();
                        break;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem toggleservice = menu.findItem(R.id.toggleservice);
        turnOnOff = (Switch) toggleservice.getActionView();
        turnOnOff.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                throwCalls("1");
//                    status.setText("SERVICE ON");

            } else {
                throwCalls("0");
//                    status.setText("SERVICE OFF");

            }
        });
//
        return true;
    }

    private void throwCalls(String s) {


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

                if(response.body().getLoginstatus().equals("1")){
                    turnOnOff.setChecked(true);

                }
                else {


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

        validateStatus();
    }




    @Override
    protected void onResume() {
        super.onResume();

        validateStatus();
    }




}

