package com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedadapter.AcceptedAdapter;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedmodel.AcceptedModel;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedmodel.Order;

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


public class AcceptedOrders extends Fragment  {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    SessionManager sessionManager;
    String userId;
    List<Order> list;
    ImageView noFood;
    TextView noOrder;

    public AcceptedOrders() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accepted_orders, container, false);
        recyclerView = view.findViewById(R.id.acceptedOrdersRecycler);
        progressBar = view.findViewById(R.id.acceptedProgress);
        noFood = view.findViewById(R.id.noFoodImage);
        noOrder = view.findViewById(R.id.noFoodText);
        sessionManager = new SessionManager(getContext());
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);
        progressBar.setVisibility(View.VISIBLE);
        setupRecycler();
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecycler();
    }

    private void setupRecycler() {


        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<AcceptedModel> call = foodGenee.getAcceptedOrders("acceptedorders", userId);
        call.enqueue(new Callback<AcceptedModel>() {
            @Override
            public void onResponse(Call<AcceptedModel> call, Response<AcceptedModel> response) {

            try{

                AcceptedModel  acceptedModel = response.body();

                if(acceptedModel.getStatus().equals("0")){

                    progressBar.setVisibility(View.GONE);
                    noFood.setVisibility(View.VISIBLE);
                    noOrder.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                }
                else if(acceptedModel.getStatus().equals("1")){

                    list = acceptedModel.getOrders();
                    noFood.setVisibility(View.GONE);
                    noOrder.setVisibility(View.GONE);
                    AcceptedAdapter acceptedOrders = new AcceptedAdapter(getActivity(), list,userId);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(acceptedOrders);



                }




            }
            catch (Exception e){

                progressBar.setVisibility(View.GONE);

            }

            }

            @Override
            public void onFailure(Call<AcceptedModel> call, Throwable t) {

                progressBar.setVisibility(View.GONE);

            }
        });

    }





}
