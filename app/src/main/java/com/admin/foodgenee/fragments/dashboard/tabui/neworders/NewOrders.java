package com.admin.foodgenee.fragments.dashboard.tabui.neworders;


import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.neworderadapter.NewOrderAdapter;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.AcceptModel;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.NewOrderModel;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.Order;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.RejectModel;
import com.admin.foodgenee.loginmodel.LoginStatusModel;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrders extends Fragment implements NewOrderAdapter.OnButtonClickListener {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    SessionManager sessionManager;
    String userId;
    List<Order> mlist;
    ImageView noFood;
    TextView noOrder;
    LinearLayout mLLOffline;


    public NewOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);
        recyclerView = view.findViewById(R.id.newOrdersRecycler);
        progressBar = view.findViewById(R.id.newOrdersProgress);
        noFood = view.findViewById(R.id.noFoodImage);
        noOrder = view.findViewById(R.id.noFoodText);
        mLLOffline=view.findViewById(R.id.ll_offline);
        sessionManager = new SessionManager(getContext());
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);

        //setupRecycler();

        return  view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if(getContext()!=null)
                validateStatus();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        validateStatus();

    }

    private void setupRecyclerList() {
    }


    private void validateStatus() {

        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<LoginStatusModel> call = foodGenee.checkLoginStatus("checkloginstatus", userId);
        call.enqueue(new Callback<LoginStatusModel>() {
            @Override
            public void onResponse(Call<LoginStatusModel> call, Response<LoginStatusModel> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.body()!=null){

                    if(response.body().getLoginstatus()!=null){
                        if(response.body().getLoginstatus().equals("1")){
                            mLLOffline.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            setupRecycler();



                        } else{    mLLOffline.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            noFood.setVisibility(View.GONE);
                            noOrder.setVisibility(View.GONE);
                        }
                    }

                }


            }

            @Override
            public void onFailure(Call<LoginStatusModel> call, Throwable t) {

            }
        });


    }


    private void setupRecycler() {
        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<NewOrderModel> call = foodGenee.getNewOrders("neworders", userId);

        call.enqueue(new Callback<NewOrderModel>() {
            @Override
            public void onResponse(Call<NewOrderModel> call, Response<NewOrderModel> response) {

                try{



                    NewOrderModel newOrderModel = response.body();


                    if(newOrderModel.getStatus().equals("0")){

                        noFood.setVisibility(View.VISIBLE);
                        noOrder.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.GONE);

                    }
                    else if(newOrderModel.getStatus().equals("1")){
                        noFood.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mlist = newOrderModel.getOrders();
                        NewOrderAdapter adapter = new NewOrderAdapter(getContext(), mlist, NewOrders.this);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);

                    }





                }
                catch (Exception e){
                    progressBar.setVisibility(View.INVISIBLE);
                    noFood.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<NewOrderModel> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
                noFood.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

            }
        });
    }else Toast.makeText(getContext(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();



}


    @Override
    public void onButtonClick(int position, String consent) {
        mlist.get(position);

        if(consent.equals("accept")){

            acceptTheOrder(mlist.get(position));

        }
        else if(consent.equals("reject")){

            rejectOrder(mlist.get(position));

        }

    }

    private void rejectOrder(Order order) {
        if(isNetworkAvailable()){
            FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
            Call<RejectModel> call = foodGenee.rejecttNewOrder("rejectorder", order.getOrderId(), userId);
            call.enqueue(new Callback<RejectModel>() {
                @Override
                public void onResponse(Call<RejectModel> call, Response<RejectModel> response) {

                    Toast.makeText(getContext(), "Order Rejected", Toast.LENGTH_SHORT).show();
                    setupRecycler();
                }

                @Override
                public void onFailure(Call<RejectModel> call, Throwable t) {

                }
            });
        }else Toast.makeText(getContext(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();


    }
    public boolean isNetworkAvailable() {
 ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
 NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
 return activeNetworkInfo != null && activeNetworkInfo.isConnected();
 }

    private void acceptTheOrder(Order order) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom);
        //dialog.setCancelable(false);
        EditText text = dialog.findViewById(R.id.et_time);
        Button submit = dialog.findViewById(R.id.b_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(text.getText().toString().length()>0&&text.getText().toString()!=null&&text.getText().toString()!=""){
                    if(isNetworkAvailable()){
                    FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
                    Call<AcceptModel> call = foodGenee.acceptNewOrder("acceptorder", order.getOrderId(), userId,Integer.valueOf(text.getText().toString()));
                    call.enqueue(new Callback<AcceptModel>() {
                        @Override
                        public void onResponse(Call<AcceptModel> call, Response<AcceptModel> response) {

                            try {
                                dialog.dismiss();
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                setupRecycler();

                            }
                            catch (Exception e)
                            {

                            }
                        }

                        @Override
                        public void onFailure(Call<AcceptModel> call, Throwable t) {

                        }
                    });
                }else Toast.makeText(getContext(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();



    }
}
