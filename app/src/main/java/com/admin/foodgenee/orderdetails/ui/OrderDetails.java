package com.admin.foodgenee.orderdetails.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.orderdetails.adapter.DetailAdapter;
import com.admin.foodgenee.orderdetails.model.OrderDetailsModel;

import java.util.HashMap;

import network.FoodGenee;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;

public class OrderDetails extends AppCompatActivity {

    TextView storeName, orderReference, totalAmount;
    SessionManager sessionManager;
    String userId;
    ImageButton goBack;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Intent intent = getIntent();
        sessionManager = new SessionManager(this);
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);
        String orderId = intent.getStringExtra("orderId");
        goBack = findViewById(R.id.goBack);
        goBack.setOnClickListener(v -> finish());
        initViews();
        drawViews(orderId);
    }

    private void drawViews(String orderId) {

        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<OrderDetailsModel> call = foodGenee.orderDetails("order", orderId, userId);
        call.enqueue(new Callback<OrderDetailsModel>() {
            @Override
            public void onResponse(Call<OrderDetailsModel> call, Response<OrderDetailsModel> response) {
                try{

                    OrderDetailsModel retrivedData = response.body();
                    if(retrivedData.getStatus().equals("0")){


                    }
                    else if(retrivedData.getStatus().equals("1")){
                    storeName.setText(retrivedData.getOrders().getStorename());
                    orderReference.setText(retrivedData.getOrders().getUniqueId());
                    totalAmount.setText("Rs." + retrivedData.getOrders().getTotalamount());
                    recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetails.this));
                    DetailAdapter adapter = new DetailAdapter(retrivedData.getOrders().getProducts());

                    recyclerView.setAdapter(adapter);

                    }

                }
                catch (Exception e){


                }
            }

            @Override
            public void onFailure(Call<OrderDetailsModel> call, Throwable t) {

            }
        });

    }


    private void initViews() {
        recyclerView = findViewById(R.id.detailOrderRecycler);
        storeName = findViewById(R.id.orderStoreName);
        orderReference = findViewById(R.id.orderId);
        totalAmount = findViewById(R.id.totalAmount);


    }




}
