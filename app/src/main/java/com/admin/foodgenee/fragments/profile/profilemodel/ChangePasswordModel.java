package com.admin.foodgenee.fragments.profile.profilemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("text")
    @Expose
    private String text;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}