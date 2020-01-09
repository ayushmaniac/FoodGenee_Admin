package com.admin.foodgenee.fragments.todaysorder.todaysadpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.orders.OrdersModel.Order;
import com.admin.foodgenee.orderdetails.ui.OrderDetails;

import java.util.List;

public class TodaysAdapter extends RecyclerView.Adapter<TodaysAdapter.TodaysViewHolder>{

    List<Order> list;
    Context context;

    public TodaysAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TodaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dateorder_row, parent, false);
        TodaysViewHolder viewHolder = new TodaysViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodaysViewHolder holder, int position) {


        try {
            holder.nOrderName.setText(list.get(position).getStorename());
            holder.nOrderLine.setText(list.get(position).getOrderline());
            holder.nOrderTable.setText(list.get(position).getTablename());
            holder.nOrderAmount.setText("Total Amount: "+list.get(position).getTotalamount());
            holder.nOrderDate.setText(list.get(position).getRegdate());
            holder.nOrderId.setText("Order ID: "+ list.get(position).getOrderId());
            holder.nOrderPaymentType.setText("Payment Type: "+ list.get(position).getPaymenttype());
            holder.cardView.setOnClickListener(v -> {

                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("orderId", list.get(position).getOrderId());
                context.startActivity(intent);

            });



        }
        catch (Exception e){



        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TodaysViewHolder extends RecyclerView.ViewHolder {
        TextView nOrderName, nOrderDate, nOrderId, nOrderAmount,nOrderTable, nOrderLine, nOrderPaymentType;
        CardView cardView;
        public TodaysViewHolder(@NonNull View itemView) {
            super(itemView);

            nOrderName = itemView.findViewById(R.id.newOrderUserName);
            nOrderDate = itemView.findViewById(R.id.newOrderDate);
            nOrderId = itemView.findViewById(R.id.newOrderId);
            nOrderAmount = itemView.findViewById(R.id.newOrderAmount);
            nOrderTable = itemView.findViewById(R.id.newOrderTableNo);
            nOrderLine = itemView.findViewById(R.id.newOrderRandomText);
            nOrderPaymentType = itemView.findViewById(R.id.newOrderPaidStatusText);
            cardView = itemView.findViewById(R.id.orderCard);
        }
    }
}
