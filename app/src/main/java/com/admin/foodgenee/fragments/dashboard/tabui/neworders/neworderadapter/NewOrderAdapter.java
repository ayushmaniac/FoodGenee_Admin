package com.admin.foodgenee.fragments.dashboard.tabui.neworders.neworderadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.NewOrderListAdapter;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.Order;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewOrderAdapter extends  RecyclerView.Adapter<NewOrderAdapter.NewViewHolder>{

    Context context;
    List<Order> list;
    private OnButtonClickListener onButtonClickListener;

    public NewOrderAdapter(Context context, List<Order> list, OnButtonClickListener onButtonClickListener) {
        this.context = context;
        this.list = list;
        this.onButtonClickListener = onButtonClickListener;
    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.neworders_newl, parent, false);
        NewViewHolder newViewHolder = new NewViewHolder(view, onButtonClickListener);
        return newViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {

        List<Product> listTwo = list.get(position).getProducts();

        try {
//            holder.nOrderName.setText(list.get(position).getStorename());
            holder.nOrderLine.setText("Line No : "+list.get(position).getOrderline());
            holder.nOrderTable.setText("Table No: "+list.get(position).getTablename());
            holder.nOrderAmount.setText("Total Amount: "+list.get(position).getTotalamount());
            holder.nOrderDate.setText(list.get(position).getRegdate());
            holder.nOrderId.setText("Order No: "+ list.get(position).getUniqueId());
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
            NewOrderListAdapter adapter = new NewOrderListAdapter(listTwo);
            holder.recyclerView.setAdapter(adapter);
//
//            if(list.get(position).getOrderprocess().equals("0")){
//
//                holder.nPaidSwitch.setChecked(false);
//            }
//            else if(list.get(position).getOrderprocess().equals("1")){
//
//                holder.nPaidSwitch.setChecked(true);
//            }

        }
        catch (Exception e){



        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nOrderName, nOrderDate, nOrderId, nOrderAmount,nOrderTable, nOrderLine;
//        Switch nPaidSwitch;
        Button nAcceptButton, nRejectButton;
        OnButtonClickListener buttonClickListener;
        RecyclerView recyclerView;

        public NewViewHolder(@NonNull View itemView, OnButtonClickListener buttonClickListener) {
            super(itemView);
            this.buttonClickListener = buttonClickListener;
//            nOrderName = itemView.findViewById(R.id.newOrderUserName);
            nOrderDate = itemView.findViewById(R.id.orderDateNew);
            nOrderId = itemView.findViewById(R.id.orderIdNew);
            nOrderAmount = itemView.findViewById(R.id.orderIdValue);
            nOrderTable = itemView.findViewById(R.id.tableNumberNew);
            nOrderLine = itemView.findViewById(R.id.orderPaymentStatus);
//            nPaidSwitch = itemView.findViewById(R.id.newOrderSwitch);
            nAcceptButton = itemView.findViewById(R.id.acceptOrderNew);
            nRejectButton = itemView.findViewById(R.id.rejectOrderNew);
            nAcceptButton.setOnClickListener(this);
            nRejectButton.setOnClickListener(this);
            recyclerView = itemView.findViewById(R.id.productsRecycler);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == nAcceptButton.getId()){
                buttonClickListener.onButtonClick(getAdapterPosition(), "accept");
                removeAt(getAdapterPosition());
            }

            else if(v.getId() == nRejectButton.getId()){

                buttonClickListener.onButtonClick(getAdapterPosition(), "reject");
                removeAt(getAdapterPosition());

            }
        }
    }


    public interface OnButtonClickListener{
        void onButtonClick(int position, String consent);
    }

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
}
