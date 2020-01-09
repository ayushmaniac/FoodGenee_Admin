package com.admin.foodgenee.orderdetails.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.foodgenee.R;
import com.admin.foodgenee.orderdetails.model.Product;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    List<Product> list;

    public DetailAdapter(List<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        DetailViewHolder detailViewHolder = new DetailViewHolder(view);
        return detailViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {

        holder.productPrice.setText("Rs. " + list.get(position).getPrice());
        holder.productCount.setText("X "+list.get(position).getCount());
        holder.productName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productCount;
        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCount = itemView.findViewById(R.id.productCount);
        }

    }
}
