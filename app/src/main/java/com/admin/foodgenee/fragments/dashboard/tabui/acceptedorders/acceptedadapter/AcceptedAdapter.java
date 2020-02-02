package com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedadapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedmodel.DeliverModel;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedmodel.Order;
import com.admin.foodgenee.orderdetails.ui.OrderDetails;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import network.FoodGenee;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedAdapter extends RecyclerView.Adapter<AcceptedAdapter.AcceptedViewHolder> {

    Context context;
    List<Order> list;
    String userId;
    private RecyclerItemClickListener recyclerItemClickListener;
    public AcceptedAdapter(Context context, List<Order> list,String userId) {
        this.context = context;
        this.list = list;
        this.userId = userId;
    }

    @NonNull
    @Override
    public AcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.acceptorder_row, parent, false);
        AcceptedViewHolder acceptedViewHolder = new AcceptedViewHolder(view);
        return acceptedViewHolder;

    }
    public void setOnClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedViewHolder holder, int position) {

        try {
            holder.nOrderName.setText(list.get(position).getUsername());
            holder.nOrderLine.setText("No :"+list.get(position).getOrderline());
            holder.nOrderTable.setText("Table No : "+list.get(position).getTablename());
            holder.nOrderAmount.setText("₹ "+list.get(position).getTotalamount());
            holder.nOrderDate.setText("Date : "+list.get(position).getRegdate());
            holder.nOrderId.setText("Order ID: "+ list.get(position).getUniqueId());
            if(list.get(position).getOrderprocess().equalsIgnoreCase("2")||
                    list.get(position).getOrderprocess().equalsIgnoreCase("4")){
                holder.mTvStatus.setText("Delivered");
                holder.deliverButton.setVisibility(View.GONE);
            }else if(list.get(position).getOrderprocess().equalsIgnoreCase("3")){
                holder.mTvStatus.setText("Canceled");
                holder.deliverButton.setVisibility(View.GONE);
            }else {holder.deliverButton.setVisibility(View.VISIBLE);
            holder.mTvStatus.setVisibility(View.GONE);}

            if(list.get(position).getPaidstatus().equals("0")){
                holder.newOrderPaidStatusText.setText("Payment Status ");
                holder.nPaidSwitch.setVisibility(View.VISIBLE);
                holder.nPaidSwitch.setChecked(false);
            }else{
                holder.nPaidSwitch.setVisibility(View.GONE);
                holder.newOrderPaidStatusText.setText("Payment Status : Paid");
            }

            if(list.get(position).getReorder().equalsIgnoreCase("1"))
                holder.mTvReorder.setVisibility(View.VISIBLE);
            else holder.mTvReorder.setVisibility(View.GONE);

            holder.mLLItem.setOnClickListener(v -> {

                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("orderId", list.get(position).getOrderId());
                context.startActivity(intent);
            });

            holder.nPaidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        if(isNetworkAvailable()){
                            FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
                            Call<DeliverModel> call = foodGenee.deliverOrder("paidstatus", list.get(position).getOrderId(),userId);
                            call.enqueue(new Callback<DeliverModel>() {
                                @Override
                                public void onResponse(Call<DeliverModel> call, Response<DeliverModel> response) {

                                    try{
                                        if(response.body().getStatus().equalsIgnoreCase("1")) {

                                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            holder.nPaidSwitch.setVisibility(View.GONE);
                                            holder.newOrderPaidStatusText.setText("Payment Status : Paid");
                                        }else if(response.body().getStatus().equalsIgnoreCase("0")) {
                                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            holder.newOrderPaidStatusText.setText("Payment Status ");
                                            holder.nPaidSwitch.setVisibility(View.VISIBLE);
                                            holder.nPaidSwitch.setChecked(false);
                                        }


                                    }
                                    catch (Exception e){


                                    }
                                }

                                @Override
                                public void onFailure(Call<DeliverModel> call, Throwable t) {

                                }
                            });
                        }else Toast.makeText(context, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();


                    }else{
                        holder.newOrderPaidStatusText.setText("Payment Status ");
                        holder.nPaidSwitch.setVisibility(View.VISIBLE);
                        holder.nPaidSwitch.setChecked(false);
                    }

                }
            });


            holder.deliverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerItemClickListener.onItemClickListener(position);


                }
            });

        }
        catch (Exception e){



        }

    }

    public boolean isNetworkAvailable() {
 ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
 NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
 return activeNetworkInfo != null && activeNetworkInfo.isConnected();
 }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class AcceptedViewHolder extends RecyclerView.ViewHolder {

        TextView nOrderName, nOrderDate, nOrderId, nOrderAmount,nOrderTable, nOrderLine,
                newOrderPaidStatusText,mTvStatus,mTvReorder;
        Switch nPaidSwitch;
        Button deliverButton;
        LinearLayout mLLItem;
        public AcceptedViewHolder(@NonNull View itemView) {
            super(itemView);
            nOrderName = itemView.findViewById(R.id.acceptOrderUserName);
            nOrderDate = itemView.findViewById(R.id.acceptOrderDate);
            nOrderId = itemView.findViewById(R.id.acceptOrderId);
            nOrderAmount = itemView.findViewById(R.id.acceptOrderAmount);
            nOrderTable = itemView.findViewById(R.id.acceptOrderTableNo);
            nOrderLine = itemView.findViewById(R.id.acceptOrderRandomText);
            nPaidSwitch = itemView.findViewById(R.id.acceptOrderSwitch);
            deliverButton = itemView.findViewById(R.id.acceptOrderDeliverButton);
            newOrderPaidStatusText=itemView.findViewById(R.id.newOrderPaidStatusText);
            mTvStatus=itemView.findViewById(R.id.tv_status);
            mLLItem=itemView.findViewById(R.id.ll_item);
            mTvReorder=itemView.findViewById(R.id.tv_order_status);


        }



    }



    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
}
