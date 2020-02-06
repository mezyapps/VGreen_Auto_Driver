package com.mezyapps.vgreen_autodriver.model;

public class UserProfileModel {

    private String id;
    private String driver_name;
    private String vehicle_no;
    private String driver_mobile_no;
    private String location;
    private String route;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getDriver_mobile_no() {
        return driver_mobile_no;
    }

    public void setDriver_mobile_no(String driver_mobile_no) {
        this.driver_mobile_no = driver_mobile_no;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
