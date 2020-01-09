package com.admin.foodgenee.fragments.dashboard.tabui.neworders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.Product;

import java.util.List;

public class NewOrderListAdapter extends  RecyclerView.Adapter<NewOrderListAdapter.NewOrderProudcts>{

    List<Product> list;

    public NewOrderListAdapter(List<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NewOrderProudcts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pro, parent, false);
        NewOrderProudcts newOrderProudcts = new NewOrderProudcts(view);
        return newOrderProudcts;
        }

    @Override
    public void onBindViewHolder(@NonNull NewOrderProudcts holder, int position) {
        holder.textView.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewOrderProudcts extends RecyclerView.ViewHolder {
        TextView textView;
        public NewOrderProudcts(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.productNameHere);

        }
    }
}
