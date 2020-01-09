package com.admin.foodgenee.forgotpassword.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.forgotpassword.model.ChangePasswordModel;

import network.FoodGenee;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    EditText confirmPasswordEt;
    EditText newPasswordEt;
    Button proceed;
    Intent get;
    String usersId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initViews();
        get = getIntent();
        usersId = get.getStringExtra("usersid");
        proceed.setOnClickListener(v -> {

            if(newPasswordEt.getText().toString().equals(confirmPasswordEt.getText().toString().trim())){

                FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
                Call<ChangePasswordModel> call = foodGenee.changePasswordSplash("changepassword", usersId, newPasswordEt.getText().toString());
                call.enqueue(new Callback<ChangePasswordModel>() {
                    @Override
                    public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {

                        try{

                            if(response.body().getStatus().equals("1")){

                                Toast.makeText(ChangePassword.this, response.body().getText(), Toast.LENGTH_SHORT).show();

                            }
                            else {

                                Toast.makeText(ChangePassword.this, response.body().getText(), Toast.LENGTH_SHORT).show();

                            }

                        }
                        catch (Exception e){


                            Toast.makeText(ChangePassword.this, "Some eror occured", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                        Toast.makeText(ChangePassword.this, "Some eror occured", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }

    private void initViews() {

        newPasswordEt = findViewById(R.id.newPasswordChange);
        confirmPasswordEt = findViewById(R.id.confirmPasswordChange);
        proceed = findViewById(R.id.confirmPasswordButton);
    }
}
