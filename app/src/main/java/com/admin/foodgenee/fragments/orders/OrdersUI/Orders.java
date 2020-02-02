package com.admin.foodgenee.fragments.orders.OrdersUI;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.orders.OrdersAdapters.OrdersAdapter;
import com.admin.foodgenee.fragments.orders.OrdersModel.Order;
import com.admin.foodgenee.fragments.orders.OrdersModel.OrdersModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    String changeMonth;

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

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String str = simpleDateFormat.format(new Date());
        if(isNetworkAvailable())
        startRecyclerCall(str);
        else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();


        selectDateButton.setOnClickListener(v -> {
            selectDateFunction();
        });
        selectDate.setOnClickListener(v -> {
                selectDateFunction();
            });

        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

            if(String.valueOf(month).length()==1){
                changeMonth="0"+month;
               // month=Integer.valueOf(str);
            }else changeMonth=String.valueOf(month);

            String datevalue = year + "-" + changeMonth + "-" + day;
            Log.e("selected Date",datevalue);
            if(isNetworkAvailable())
            startRecyclerCall(datevalue);
            else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();



        };
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String str = simpleDateFormat.format(new Date());
        startRecyclerCall(str);*/
    }

    private void selectDateFunction() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if(String.valueOf(month).length()==1){
            changeMonth="0"+month;
            // month=Integer.valueOf(str);
        }else changeMonth=String.valueOf(month);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public boolean isNetworkAvailable() {
 ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
 NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
 return activeNetworkInfo != null && activeNetworkInfo.isConnected();
 }

    private void startRecyclerCall(String date) {
        Log.e("selected Date",date);
        progressBar.setVisibility(View.VISIBLE);
        dateText.setVisibility(View.GONE);
        selectDateButton.setVisibility(View.GONE);
        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<OrdersModel> call = foodGenee.ordersByDate("orderslist", date,userId );
        call.enqueue(new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {

                try {
                    OrdersModel model = response.body();
                    if(model.getStatus().equals("0")){
                        noOrdersDate.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        noOrdersDate.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                    else if(model.getStatus().equals("1")){

                        progressBar.setVisibility(View.GONE);
                        if(model.getOrders().size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            OrdersAdapter ordersAdapter = new OrdersAdapter(model.getOrders(), getContext());
                            recyclerView.setAdapter(ordersAdapter);
                            noOrdersDate.setVisibility(View.GONE);
                            selectDateButton.setVisibility(View.GONE);

                        }else{
                            noOrdersDate.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }



                    }


                }
                catch (Exception e){

                    progressBar.setVisibility(View.GONE);
                    noOrdersDate.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                    //selectDateButton.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                noOrdersDate.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                //selectDateButton.setVisibility(View.VISIBLE);


            }
        });
    }




}
