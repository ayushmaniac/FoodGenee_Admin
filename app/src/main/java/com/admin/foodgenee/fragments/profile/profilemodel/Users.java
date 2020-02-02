package com.admin.foodgenee.fragments.profile.profilemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Users {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("profilepic")
    @Expose
    private String profilepic;

    @SerializedName("storename")
    @Expose
    private String storename;

    @SerializedName("totalorders")
    @Expose
    private String totalorders;

    @SerializedName("todayorders")
    @Expose
    private String todayorders;

    @SerializedName("totalpoints")
    @Expose
    private String totalpoints;

    public String getTotalorders() {
        return totalorders;
    }

    public void setTotalorders(String totalorders) {
        this.totalorders = totalorders;
    }

    public String getTodayorders() {
        return todayorders;
    }

    public void setTodayorders(String todayorders) {
        this.todayorders = todayorders;
    }

    public String getTotalpoints() {
        return totalpoints;
    }

    public void setTotalpoints(String totalpoints) {
        this.totalpoints = totalpoints;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

}