package com.admin.foodgenee.fragments.dashboard.tabui.neworders;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.neworderadapter.NewOrderAdapter;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.AcceptModel;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.NewOrderModel;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.Order;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.RejectModel;

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
        sessionManager = new SessionManager(getContext());
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);
        progressBar.setVisibility(View.VISIBLE);
        setupRecycler();
        setupRecyclerList();
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecycler();
    }

    private void setupRecyclerList() {
    }

    private void setupRecycler() {

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


                    }
                    else if(newOrderModel.getStatus().equals("1")){
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mlist = newOrderModel.getOrders();
                        NewOrderAdapter adapter = new NewOrderAdapter(getContext(), mlist, NewOrders.this);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);



                    }





                }
                catch (Exception e){
                    progressBar.setVisibility(View.INVISIBLE);


                }

            }

            @Override
            public void onFailure(Call<NewOrderModel> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);

            }
        });


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

        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<RejectModel> call = foodGenee.rejecttNewOrder("rejectorder", order.getOrderId(), userId);
       call.enqueue(new Callback<RejectModel>() {
           @Override
           public void onResponse(Call<RejectModel> call, Response<RejectModel> response) {

               Toast.makeText(getContext(), "Order Rejected", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onFailure(Call<RejectModel> call, Throwable t) {

           }
       });

    }

    private void acceptTheOrder(Order order) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom);
        dialog.setCancelable(false);
        EditText text = dialog.findViewById(R.id.et_time);
        Button submit = dialog.findViewById(R.id.b_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(text.getText().toString().length()>0&&text.getText().toString()!=null&&text.getText().toString()!=""){

                    FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
                    Call<AcceptModel> call = foodGenee.acceptNewOrder("acceptorder", order.getOrderId(), userId,Integer.valueOf(text.getText().toString()));
                    call.enqueue(new Callback<AcceptModel>() {
                        @Override
                        public void onResponse(Call<AcceptModel> call, Response<AcceptModel> response) {

                            try {
                                dialog.dismiss();
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception e)
                            {

                            }
                        }

                        @Override
                        public void onFailure(Call<AcceptModel> call, Throwable t) {

                        }
                    });
                }

            }
        });

        dialog.show();



    }
}
