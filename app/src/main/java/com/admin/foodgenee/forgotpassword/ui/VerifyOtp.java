package com.admin.foodgenee.forgotpassword.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.forgotpassword.model.VerifyModel;
import com.chaos.view.PinView;

import network.FoodGenee;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtp extends AppCompatActivity {

    EditText et1,et2,et3,et4;
    Button proceed;
    Intent get;
    String userId;
    Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        initViews();
        get = getIntent();
        userId = get.getStringExtra("usersid");
        proceed.setOnClickListener(v -> {
            loadingDialog = new Dialog(VerifyOtp.this);
            loadingDialog.setContentView(R.layout.loading_dialog);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        String otp = et1.getText().toString().trim()+et2.getText().toString().trim()+et3.getText().toString().trim()+et4.getText().toString().trim();

            Call<VerifyModel> call = foodGenee.verifyOtp("forgotpassword-otp", userId, otp);
            call.enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {

                    try {


                        if (response.body().getStatus().equals("1")) {
                            loadingDialog.cancel();
                            loadingDialog.dismiss();
                            Toast.makeText(VerifyOtp.this, response.body().getText(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerifyOtp.this, ChangePassword.class);
                            intent.putExtra("usersid", response.body().getUsersid());
                            startActivity(intent);

                        } else if (response.body().getStatus().equals("0")) {

                            loadingDialog.cancel();
                            loadingDialog.dismiss();
                            Toast.makeText(VerifyOtp.this, response.body().getText(), Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {

                        loadingDialog.cancel();
                        loadingDialog.dismiss();

                    }
                }
                @Override
                public void onFailure(Call<VerifyModel> call, Throwable t) {
                    loadingDialog.cancel();
                    loadingDialog.dismiss();

                }
            });

        });



}

    private void initViews() {

        proceed = findViewById(R.id.verifyOtpForgotButton);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et2.requestFocus();
                } else if (s.length() == 0) {
                    et1.clearFocus();
                }


            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et3.requestFocus();
                } else if (s.length() == 0) {
                    et2.clearFocus();
                }


            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et4.requestFocus();
                } else if (s.length() == 0) {
                    et3.clearFocus();
                }


            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    et3.requestFocus();
                }

            }
        });





    }
}
