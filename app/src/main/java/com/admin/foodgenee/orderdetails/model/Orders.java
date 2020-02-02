package com.admin.foodgenee.orderdetails.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Orders {


        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("unique_id")
        @Expose
        private String uniqueId;
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
         @Expose
         @SerializedName("tax")
         private String tax;
         @Expose
         @SerializedName("tips")
         private String tips;
         @Expose
        @SerializedName("subscription")
        private String subscription;
         @Expose
        @SerializedName("couponamount")
         private String couponamount;
        @Expose
        @SerializedName("coverpic")
        private String coverpic;
        @Expose
        @SerializedName("logo")
        private String logo;
        @Expose
        @SerializedName("username")
        private String username;

    @Expose
    @SerializedName("orderprocesstext")
    private String orderprocesstext;

    @Expose
    @SerializedName("pendingamount")
    private String pendingamount;


    public String getPendingamount() {
        return pendingamount;
    }

    public void setPendingamount(String pendingamount) {
        this.pendingamount = pendingamount;
    }

    public String getOrderprocesstext() {
        return orderprocesstext;
    }

    public void setOrderprocesstext(String orderprocesstext) {
        this.orderprocesstext = orderprocesstext;
    }

    public String getCoverpic() {
        return coverpic;
    }

    public void setCoverpic(String coverpic) {
        this.coverpic = coverpic;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @SerializedName("products")
        @Expose
        private List<Product> products = null;



    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getCouponamount() {
        return couponamount;
    }

    public void setCouponamount(String couponamount) {
        this.couponamount = couponamount;
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

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
}
