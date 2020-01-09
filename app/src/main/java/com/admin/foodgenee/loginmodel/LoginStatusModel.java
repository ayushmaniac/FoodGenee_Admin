package com.admin.foodgenee.loginmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginStatusModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("loginstatus")
    @Expose
    private String loginstatus;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginstatus() {
        return loginstatus;
    }

    public void setLoginstatus(String loginstatus) {
        this.loginstatus = loginstatus;
    }

}
