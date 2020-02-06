package com.mezyapps.vgreen_autodriver.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SuccessModel {
    private String code;
    private String msg;
    private String folder;

    @SerializedName("login")
    ArrayList<UserProfileModel> userProfileModelArrayList;

    @SerializedName("active_order")
    ArrayList<CommonDeliveryModel> commonDeliveryModelArrayList;

    @SerializedName("order_head_list")
    private ArrayList<OrderHistoryDTModel> orderHistoryDTModelArrayList;


    @SerializedName("notification_list")
    private ArrayList<NotificationModel> notificationModelArrayList;


    public ArrayList<UserProfileModel> getUserProfileModelArrayList() {
        return userProfileModelArrayList;
    }

    public void setUserProfileModelArrayList(ArrayList<UserProfileModel> userProfileModelArrayList) {
        this.userProfileModelArrayList = userProfileModelArrayList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<CommonDeliveryModel> getCommonDeliveryModelArrayList() {
        return commonDeliveryModelArrayList;
    }

    public void setCommonDeliveryModelArrayList(ArrayList<CommonDeliveryModel> commonDeliveryModelArrayList) {
        this.commonDeliveryModelArrayList = commonDeliveryModelArrayList;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public ArrayList<OrderHistoryDTModel> getOrderHistoryDTModelArrayList() {
        return orderHistoryDTModelArrayList;
    }

    public void setOrderHistoryDTModelArrayList(ArrayList<OrderHistoryDTModel> orderHistoryDTModelArrayList) {
        this.orderHistoryDTModelArrayList = orderHistoryDTModelArrayList;
    }

    public ArrayList<NotificationModel> getNotificationModelArrayList() {
        return notificationModelArrayList;
    }

    public void setNotificationModelArrayList(ArrayList<NotificationModel> notificationModelArrayList) {
        this.notificationModelArrayList = notificationModelArrayList;
    }
}
