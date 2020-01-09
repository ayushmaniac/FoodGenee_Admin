package com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedmodel.Order;

import java.util.List;

public class AcceptedAdapter extends RecyclerView.Adapter<AcceptedAdapter.AcceptedViewHolder> {

    Context context;
    List<Order> list;
    OnDeliverClickListner onDeliverClickListner;

    public AcceptedAdapter(Context context, List<Order> list, OnDeliverClickListner onDeliverClickListner) {
        this.context = context;
        this.list = list;
        this.onDeliverClickListner = onDeliverClickListner;
    }

    @NonNull
    @Override
    public AcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.acceptorder_row, parent, false);
        AcceptedViewHolder acceptedViewHolder = new AcceptedViewHolder(view, onDeliverClickListner);
        return acceptedViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedViewHolder holder, int position) {

        try {
            holder.nOrderName.setText(list.get(position).getStorename());
            holder.nOrderLine.setText(list.get(position).getOrderline());
            holder.nOrderTable.setText(list.get(position).getTablename());
            holder.nOrderAmount.setText("Total Amount: "+list.get(position).getTotalamount());
            holder.nOrderDate.setText(list.get(position).getRegdate());
            holder.nOrderId.setText("Order ID: "+ list.get(position).getOrderId());

            if(list.get(position).getOrderprocess().equals("0")){

                holder.nPaidSwitch.setChecked(false);
            }

        }
        catch (Exception e){



        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class AcceptedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nOrderName, nOrderDate, nOrderId, nOrderAmount,nOrderTable, nOrderLine;
        Switch nPaidSwitch;
        Button deliverButton;
        OnDeliverClickListner clickListner;
        public AcceptedViewHolder(@NonNull View itemView, OnDeliverClickListner clickListner) {
            super(itemView);
            nOrderName = itemView.findViewById(R.id.acceptOrderUserName);
            nOrderDate = itemView.findViewById(R.id.acceptOrderDate);
            nOrderId = itemView.findViewById(R.id.acceptOrderId);
            nOrderAmount = itemView.findViewById(R.id.acceptOrderAmount);
            nOrderTable = itemView.findViewById(R.id.acceptOrderTableNo);
            nOrderLine = itemView.findViewById(R.id.acceptOrderRandomText);
            nPaidSwitch = itemView.findViewById(R.id.acceptOrderSwitch);
            deliverButton = itemView.findViewById(R.id.acceptOrderDeliverButton);
            this.clickListner = clickListner;
            deliverButton.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
        clickListner.onClick(getAdapterPosition());
        removeAt(getAdapterPosition());

        }
    }

    public interface OnDeliverClickListner{
        void onClick(int position);

    }

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
}
