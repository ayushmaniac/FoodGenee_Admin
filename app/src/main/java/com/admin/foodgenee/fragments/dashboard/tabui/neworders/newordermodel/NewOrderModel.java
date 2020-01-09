package com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewOrderModel {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
