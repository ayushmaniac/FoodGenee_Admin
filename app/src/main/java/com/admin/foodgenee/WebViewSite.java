package com.admin.foodgenee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import network.FoodGenee;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;


public class WebViewSite extends AppCompatActivity  {

    private static final String TAG = "FIREBASE";
    Switch turnOnOff;
    String firebaseToken;
    Intent intent;
    String webUrl = null;
    WebView webView;
    SessionManager sessionManager;
    Boolean click = false;
    TextView status;
    String url;
    ProgressBar progressBar;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        sessionManager = new SessionManager(this);
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
         url = getUrl.get(sessionManager.WEB);
         userId = getUrl.get(sessionManager.USER_ID);
         intent = getIntent();
        webView = findViewById(R.id.webView);
        status = findViewById(R.id.serviceStatus);
        loadWebView(url);
        turnOnOff = findViewById(R.id.turnbutton);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setClickable(true);
        webView.setWebChromeClient(new WebChromeClient());
        getFirebaseToken();
        status.setOnClickListener(view -> loadWebView(url));
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
//        turnOnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(click = false){
//                    clickStatus = "1";
//                    throwCalls(clickStatus);
//                    click = true;
//                    turnOnOff.setText("Turn OFF");
//                }
//              if(click = true){
//
//                    clickStatus = "0";
//                    throwCalls(clickStatus);
//                    click = false;
//                    turnOnOff.setText("Turn ON");
//                }
//            }
//        });


    }

    private void loadWebView(String webUrl) {
        webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//            progressBar.setVisibility(View.GONE);

    }


    private void getFirebaseToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    firebaseToken = task.getResult().getToken();


                });

    }




    private void throwCalls(String clickStatus) {
        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<ResponseBody> call = foodGenee.sendStatus("login-status", userId,clickStatus, firebaseToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

//                startService(new Intent(WebViewSite.this,BroadcastService.class));

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(WebViewSite.this, "Some error occured", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){

                case R.id.logoutButtonNew:
                    sessionManager.logout();
                    return true;
            }
        return true;

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

//    public void startService(View v) {
//
//        String input = "start";
//        Intent serviceIntent = new Intent(this,BroadcastService.class);
//        serviceIntent.putExtra("inputExtra", input);
//
//        ContextCompat.startForegroundService(this, serviceIntent);
//    }

//    public void stopService(View v) {
//        Intent serviceIntent = new Intent(this, ExampleService.class);
//        stopService(serviceIntent);
//    }

}
