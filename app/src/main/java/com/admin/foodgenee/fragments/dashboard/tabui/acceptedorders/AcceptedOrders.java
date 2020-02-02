package com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedadapter.AcceptedAdapter;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedadapter.RecyclerItemClickListener;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedmodel.AcceptedModel;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedmodel.DeliverModel;
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

       // setupRecycler();
        return  view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if(getContext()!=null)
            setupRecycler();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecycler();
    }

    private void setupRecycler() {

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
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
                    recyclerView.setVisibility(View.VISIBLE);
                    AcceptedAdapter acceptedOrders = new AcceptedAdapter(getActivity(), list,userId);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(acceptedOrders);
                    acceptedOrders.setOnClickListener(new RecyclerItemClickListener() {
                        @Override
                        public void onItemClickListener(int position) {
                            if(isNetworkAvailable()){
                                FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
                                Call<DeliverModel> call = foodGenee.deliverOrder("deliverorder", list.get(position).getOrderId(),userId);
                                call.enqueue(new Callback<DeliverModel>() {
                                    @Override
                                    public void onResponse(Call<DeliverModel> call, Response<DeliverModel> response) {

                                        try{
                                            if(response.body().getStatus().equalsIgnoreCase("1")) {

                                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                setupRecycler();
                                                // removeAt(position);
                                            }else if(response.body().getStatus().equalsIgnoreCase("0")) {
                                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                        catch (Exception e){


                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DeliverModel> call, Throwable t) {

                                    }
                                });
                            }else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

                        }
                    });



                }




            }
            catch (Exception e){

                progressBar.setVisibility(View.GONE);
                noFood.setVisibility(View.VISIBLE);
                noOrder.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

            }

            }

            @Override
            public void onFailure(Call<AcceptedModel> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                noFood.setVisibility(View.VISIBLE);
                noOrder.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

            }
        });
        }else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();


    }

    public boolean isNetworkAvailable() {
 ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
 NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
 return activeNetworkInfo != null && activeNetworkInfo.isConnected();
 }



}
