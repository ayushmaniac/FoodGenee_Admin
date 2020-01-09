package com.admin.foodgenee.fragments.orders.OrdersUI;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.orders.OrdersAdapters.OrdersAdapter;
import com.admin.foodgenee.fragments.orders.OrdersModel.Order;
import com.admin.foodgenee.fragments.orders.OrdersModel.OrdersModel;
import com.admin.foodgenee.orderdetails.ui.OrderDetails;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import network.FoodGenee;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;

import static com.admin.foodgenee.firebaseservices.FirebaseMessaging.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Orders extends Fragment  {

    TextView selectDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button selectDateButton;
    SessionManager sessionManager;
    String userId;
    ProgressBar progressBar;
    TextView dateText, noOrdersDate;
    RecyclerView recyclerView;
    List<Order> list;


    public Orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        sessionManager = new SessionManager(getContext());
        recyclerView = view.findViewById(R.id.ordersRecycler);
        selectDateButton = view.findViewById(R.id.selectDateButton);
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);
        progressBar = view.findViewById(R.id.dateProgress);
        dateText = view.findViewById(R.id.setDateText);
        noOrdersDate = view.findViewById(R.id.noOrdersText);
        selectDate = view.findViewById(R.id.selectDateText);
        selectDateButton.setOnClickListener(v -> {
            selectDateFunction();
        });
        selectDate.setOnClickListener(v -> {
                selectDateFunction();
            });

        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

            String date = year + "-" + month + "-" + day;
            startRecyclerCall(date);
            progressBar.setVisibility(View.VISIBLE);
            dateText.setVisibility(View.GONE);
            selectDateButton.setVisibility(View.GONE);

        };
        return view;
    }

    private void selectDateFunction() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void startRecyclerCall(String date) {
        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<OrdersModel> call = foodGenee.ordersByDate("orderslist", date,userId );
        call.enqueue(new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {

                try {
                    OrdersModel model = response.body();
                    if(model.getStatus().equals("0")){
                        progressBar.setVisibility(View.GONE);
                        noOrdersDate.setVisibility(View.VISIBLE);


                    }
                    else if(model.getStatus().equals("1")){

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        OrdersAdapter ordersAdapter = new OrdersAdapter(model.getOrders(), getContext());
                        recyclerView.setAdapter(ordersAdapter);
                        noOrdersDate.setVisibility(View.GONE);
                        selectDateButton.setVisibility(View.GONE);


                    }


                }
                catch (Exception e){

                    progressBar.setVisibility(View.GONE);
                    selectDateButton.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                selectDateButton.setVisibility(View.VISIBLE);


            }
        });
    }




}
