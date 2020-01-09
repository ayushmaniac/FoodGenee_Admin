package com.admin.foodgenee.forgotpassword.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.forgotpassword.model.ForgotPasswordModel;

import network.FoodGenee;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    EditText getUsername;
    Button proceed;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        proceed.setOnClickListener(v -> {
            loadingDialog = new Dialog(ForgotPassword.this);
            loadingDialog.setContentView(R.layout.loading_dialog);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            sendOtp();
        });
    }

    private void sendOtp() {

        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<ForgotPasswordModel> call = foodGenee.forgotPassword("forgotpassword",getUsername.getText().toString().trim());
        call.enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {

                if(response.body().getStatus().equals("1")){

                    Toast.makeText(ForgotPassword.this, response.body().getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPassword.this, VerifyOtp.class);
                    intent.putExtra("usersid", response.body().getUsersid());
                    startActivity(intent);
                    loadingDialog.cancel();
                    loadingDialog.dismiss();

                }
                else if(response.body().getStatus().equals("0")){

                    Toast.makeText(ForgotPassword.this, response.body().getText(), Toast.LENGTH_SHORT).show();
                    loadingDialog.cancel();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                loadingDialog.cancel();
                loadingDialog.dismiss();

            }
        });
    }

    private void initViews() {
        getUsername = findViewById(R.id.forgotPasswordEmail);
        proceed = findViewById(R.id.getOtpButton);

    }
}
