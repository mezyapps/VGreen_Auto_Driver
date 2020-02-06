package com.mezyapps.vgreen_autodriver.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CommonDeliveryModel implements Parcelable {


    private String order_no;
    private String customer_name;
    private String delivery_address;
    private String mobile_no;
    private String total_price;
    private String total_mrp;
    private String status;
    private String date;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {


        @Override
        public CommonDeliveryModel createFromParcel(Parcel source) {
            return new CommonDeliveryModel(source);
        }

        @Override
        public CommonDeliveryModel[] newArray(int size) {
            return new CommonDeliveryModel[0];
        }
    };

    public CommonDeliveryModel(Parcel source) {
        this.order_no = source.readString();
        this.customer_name = source.readString();
        this.delivery_address = source.readString();
        this.mobile_no = source.readString();
        this.total_price = source.readString();
        this.total_mrp = source.readString();
        this.status = source.readString();
        this.date = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_no);
        dest.writeString(this.customer_name);
        dest.writeString(this.delivery_address);
        dest.writeString(this.mobile_no);
        dest.writeString(this.total_price);
        dest.writeString(this.total_mrp);
        dest.writeString(this.status);
        dest.writeString(this.date);

    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_mrp() {
        return total_mrp;
    }

    public void setTotal_mrp(String total_mrp) {
        this.total_mrp = total_mrp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
