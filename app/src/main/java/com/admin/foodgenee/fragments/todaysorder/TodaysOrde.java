package com.admin.foodgenee.fragments.todaysorder;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.orders.OrdersModel.OrdersModel;
import com.admin.foodgenee.fragments.todaysorder.todaysadpater.TodaysAdapter;

import java.util.HashMap;

import network.FoodGenee;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodaysOrde extends Fragment {

    RecyclerView recyclerView;
    TextView noTodaysOrderText;
    SessionManager sessionManager;
    String userId;
    ProgressBar progressBar;
    public TodaysOrde() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_todays_orde, container, false);
        recyclerView = view.findViewById(R.id.todaysOrderRecycler);
        sessionManager = new SessionManager(getContext());
        noTodaysOrderText = view.findViewById(R.id.noOrderToday);
        progressBar = view.findViewById(R.id.todaysOrderProgress);
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);

        setupRecyclerView();
        return  view;
    }

    private void setupRecyclerView() {

        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<com.admin.foodgenee.fragments.orders.OrdersModel.OrdersModel> call = foodGenee.ordersByDate("orderslist", "",userId );
        call.enqueue(new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {

                try{

                    OrdersModel ordersModel = response.body();
                    if(ordersModel.getStatus().equals("1")){

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    TodaysAdapter adapter = new TodaysAdapter(ordersModel.getOrders(), getContext());
                    recyclerView.setAdapter(adapter);

                    }
                    else if(ordersModel.getStatus().equals("0")){

                        progressBar.setVisibility(View.GONE);
                        noTodaysOrderText.setVisibility(View.VISIBLE);


                    }

                }
                catch (Exception e){



                }
            }

            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {

            }
        });
    }

}
