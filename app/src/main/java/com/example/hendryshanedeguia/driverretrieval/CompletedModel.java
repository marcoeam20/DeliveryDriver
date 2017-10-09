package com.example.hendryshanedeguia.driverretrieval;

/**
 * Created by macbook on 04/10/2017.
 */

public class CompletedModel {
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustUsername() {
        return custUsername;
    }

    public void setCustUsername(String custUsername) {
        this.custUsername = custUsername;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String orderID;
    public String custUsername;
    public String custAddress;

    public CompletedModel(){}
}
