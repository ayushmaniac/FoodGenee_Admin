package com.admin.foodgenee;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.forgotpassword.ui.ForgotPassword;
import com.admin.foodgenee.loginmodel.LoginModel;

import androidx.appcompat.app.AppCompatActivity;
import network.FoodGenee;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;

public class Login extends AppCompatActivity {

    EditText usernmame, password;
    Button loginHere;
    ProgressBar progressBar;
    SessionManager sessionManager;
    TextView forgotPassword;


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
        forgotPassword = findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(v -> {

            Intent intent = new Intent(Login.this, ForgotPassword.class);
            startActivity(intent);

        });

        loginHere.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (isNetworkAvailable())
            giveLoginCall();
            else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
        });

        if(sessionManager.isLoggin()){

            Intent intent = new Intent(Login.this, Root.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("type","Login");
            startActivity(intent);

            checkLoginStatus();
        }
    }
    public boolean isNetworkAvailable() {
ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
 NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
 return activeNetworkInfo != null && activeNetworkInfo.isConnected();
 }

    private void checkLoginStatus() {



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
                        Intent intent = new Intent(Login.this, Root.class);
                        intent.putExtra("url", response.body().getText());
                        intent.putExtra("type","Login");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                    else if(response.body().getStatus().equals("0")){


                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, response.body().getText(), Toast.LENGTH_SHORT).show();

                    }
                    else {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, response.body().getText(), Toast.LENGTH_SHORT).show();


                    }
                }
                catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Some error occured", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
