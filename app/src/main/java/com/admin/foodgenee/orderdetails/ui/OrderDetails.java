package com.admin.foodgenee.orderdetails.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.orderdetails.adapter.DetailAdapter;
import com.admin.foodgenee.orderdetails.model.OrderDetailsModel;
import com.bumptech.glide.Glide;

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

public class OrderDetails extends AppCompatActivity {

    TextView storeName, orderReference, totalAmount,mTvTip,mTvTax,
            mTvSubscription,mTvCoupon,mTvUser,mTvTable,mTvPaid,mTvOrderStatus,mTvPending;
    SessionManager sessionManager;
    String userId;
    ImageButton goBack;
    RecyclerView recyclerView;
    LinearLayout mLLTax,mLLTip,mLLSubscription,mLLCoupon,mLLPending;
    ImageView mIvBanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        mLLTax=findViewById(R.id.ll_tax);
        mLLTip=findViewById(R.id.ll_tip);
        mLLSubscription=findViewById(R.id.ll_subscription);
        mLLCoupon=findViewById(R.id.ll_coupon);
        mTvTip=findViewById(R.id.tv_tip);
        mTvTax=findViewById(R.id.tv_tax);
        mTvSubscription=findViewById(R.id.tv_subscription);
        mTvCoupon=findViewById(R.id.tv_couponamount);
        mIvBanner=findViewById(R.id.tv_banner);
        mTvUser=findViewById(R.id.tv_user);
        mTvTable=findViewById(R.id.tv_table);
        mTvPaid=findViewById(R.id.tv_paid_status);
        mTvOrderStatus=findViewById(R.id.tv_order_status);
        mLLPending=findViewById(R.id.ll_pending);
        mTvPending=findViewById(R.id.tv_pending);
        Intent intent = getIntent();
        sessionManager = new SessionManager(this);
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);
        String orderId = intent.getStringExtra("orderId");
        goBack = findViewById(R.id.goBack);
        goBack.setOnClickListener(v -> finish());
        initViews();
        if(isNetworkAvailable())
        drawViews(orderId);
        else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
    }
    public boolean isNetworkAvailable() {
 ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
 NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
 return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                        Glide.with(OrderDetails.this)
                                .load(retrivedData.getOrders().getCoverpic())
                                .into(mIvBanner);
                        mTvUser.setText(retrivedData.getOrders().getUsername());
                        mTvTable.setText("Table No : "+retrivedData.getOrders().getTablename());
                        mTvPaid.setText("Paid Status : "+retrivedData.getOrders().getPaidstatus());
                    storeName.setText(retrivedData.getOrders().getStorename());
                    orderReference.setText(retrivedData.getOrders().getUniqueId());
                    totalAmount.setText("₹ " + retrivedData.getOrders().getTotalamount());
                        mTvOrderStatus.setText(retrivedData.getOrders().getOrderprocesstext());
                    if(retrivedData.getOrders().getCouponamount()!=null){
                        if(retrivedData.getOrders().getCouponamount().equalsIgnoreCase("0")||
                                retrivedData.getOrders().getCouponamount().equalsIgnoreCase("")){
                            mLLCoupon.setVisibility(View.GONE);
                        }else {
                            mLLCoupon.setVisibility(View.VISIBLE);
                            mTvCoupon.setText("₹ "+retrivedData.getOrders().getCouponamount());
                        }
                    }else  mLLCoupon.setVisibility(View.GONE);

                        if(retrivedData.getOrders().getTax()!=null){
                            if(retrivedData.getOrders().getTax().equalsIgnoreCase("0")||
                                    retrivedData.getOrders().getTax().equalsIgnoreCase("")){
                                mLLTax.setVisibility(View.GONE);
                            }else {
                                mLLTax.setVisibility(View.VISIBLE);
                                mTvTax.setText("₹ "+retrivedData.getOrders().getTax());
                            }
                        }else  mLLTax.setVisibility(View.GONE);


                        if(retrivedData.getOrders().getTips()!=null){
                            if(retrivedData.getOrders().getTips().equalsIgnoreCase("0")||
                                    retrivedData.getOrders().getTips().equalsIgnoreCase("")){
                                mLLTip.setVisibility(View.GONE);
                            }else {
                                mLLTip.setVisibility(View.VISIBLE);
                                mTvTip.setText("₹ "+retrivedData.getOrders().getTips());
                            }
                        }else  mLLTip.setVisibility(View.GONE);

                        if(retrivedData.getOrders().getSubscription()!=null){
                            if(retrivedData.getOrders().getSubscription().equalsIgnoreCase("0")||
                                    retrivedData.getOrders().getSubscription().equalsIgnoreCase("")){
                                mLLSubscription.setVisibility(View.GONE);
                            }else {
                                mLLSubscription.setVisibility(View.VISIBLE);
                                mTvSubscription.setText("₹ "+retrivedData.getOrders().getSubscription());
                            }
                        }else  mLLSubscription.setVisibility(View.GONE);

                        if(retrivedData.getOrders().getPendingamount()!=null) {
                            if (retrivedData.getOrders().getPendingamount().equalsIgnoreCase("0") ||
                                    retrivedData.getOrders().getPendingamount().equalsIgnoreCase("")) {
                                mLLPending.setVisibility(View.GONE);
                            }else{  mLLPending.setVisibility(View.VISIBLE);
                                mTvPending.setText("₹ "+retrivedData.getOrders().getPendingamount());}
                        }else mLLPending.setVisibility(View.GONE);


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
