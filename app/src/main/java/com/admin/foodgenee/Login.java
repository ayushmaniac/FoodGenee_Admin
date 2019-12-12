package com.admin.foodgenee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.admin.foodgenee.loginmodel.LoginModel;

import network.FoodGenee;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;

public class Login extends AppCompatActivity {

    EditText usernmame,password;
    Button loginHere;
    ProgressBar progressBar;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernmame = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        loginHere = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.loginProgress);
        sessionManager = new SessionManager(this);
        progressBar.setVisibility(View.GONE);

        loginHere.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            giveLoginCall();
        });

        if(sessionManager.isLoggin()){

            Intent intent = new Intent(Login.this, WebViewSite.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void giveLoginCall() {

        String edname = usernmame.getText().toString();
        String edpass = password.getText().toString();

        FoodGenee api = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<LoginModel> call = api.getUser("login",edname, edpass);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                try {

                    if(response.body().getStatus().equals("1")){

                        progressBar.setVisibility(View.GONE);
                        sessionManager.createSession(response.body().getUsersid(), response.body().getText());
                        Intent intent = new Intent(Login.this, WebViewSite.class);
                        intent.putExtra("url", response.body().getText());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                    else if(response.body().getStatus().equals("0")){


                        progressBar.setVisibility(View.GONE);

                    }
                    else {

                        progressBar.setVisibility(View.GONE);

                    }
                }
                catch (Exception e){
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
