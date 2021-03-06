package com.admin.foodgenee.fragments.orders.OrdersModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersModel {


    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;


    public List<Order> getOrders() {
        return orders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
