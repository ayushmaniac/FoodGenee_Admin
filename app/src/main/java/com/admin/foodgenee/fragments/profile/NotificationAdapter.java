package com.admin.foodgenee.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.profile.profilemodel.NotificationModel;
import com.admin.foodgenee.orderdetails.ui.OrderDetails;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.DateOrdersViewHolder> {

    List<NotificationModel> list;
    Context context;

    public NotificationAdapter(List<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DateOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        DateOrdersViewHolder dateOrdersViewHolder = new DateOrdersViewHolder(view);
        return dateOrdersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DateOrdersViewHolder holder, int position) {


        try {

            holder.tv_notification.setText(list.get(position).getMessage());
            holder.tv_date.setText(list.get(position).getRegdate());
            holder.tv_title.setText(list.get(position).getTitle());
            holder.orderCard.setOnClickListener(v -> {

                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("orderId", list.get(position).getOrder_id());
                context.startActivity(intent);
            });

        } catch (Exception e) {


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DateOrdersViewHolder extends RecyclerView.ViewHolder  {
        TextView tv_notification,tv_date,tv_title;
        CardView orderCard;

        public DateOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_notification = itemView.findViewById(R.id.tv_notification);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_title=itemView.findViewById(R.id.tv_title);
            orderCard = itemView.findViewById(R.id.orderCard);
        }


    }
}