package com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {


    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("storename")
    @Expose
    private String storename;
    @SerializedName("tablename")
    @Expose
    private String tablename;
    @SerializedName("totalamount")
    @Expose
    private String totalamount;
    @SerializedName("paymenttype")
    @Expose
    private String paymenttype;
    @SerializedName("orderprocess")
    @Expose
    private String orderprocess;
    @SerializedName("paidstatus")
    @Expose
    private String paidstatus;
    @SerializedName("orderline")
    @Expose
    private String orderline;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("regdate")
    @Expose
    private String regdate;
    @SerializedName("order")
    @Expose
    private String order;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String getOrderprocess() {
        return orderprocess;
    }

    public void setOrderprocess(String orderprocess) {
        this.orderprocess = orderprocess;
    }

    public String getPaidstatus() {
        return paidstatus;
    }

    public void setPaidstatus(String paidstatus) {
        this.paidstatus = paidstatus;
    }

    public String getOrderline() {
        return orderline;
    }

    public void setOrderline(String orderline) {
        this.orderline = orderline;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

}

