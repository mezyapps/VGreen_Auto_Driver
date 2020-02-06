package com.mezyapps.vgreen_autodriver.model;

public class OrderHistoryDTModel {

    private String product_id;
    private String product_name;
    private String prod_image;
    private String qty;
    private String unit_id;
    private String unit_name;
    private String total_price;
    private String total_mrp;
    private String unit;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProd_image() {
        return prod_image;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }
}
