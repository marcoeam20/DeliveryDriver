package com.example.hendryshanedeguia.driverretrieval;

/**
 * Created by macbook on 27/09/2017.
 */

public class CompletedOrderInformation {
    public String custAddress;
    public String custContact;
    public String custImageUrl;
    public String custUsername;
    public String orderBill;
    public String orderID;
    public CompletedOrderInformation() {
    }

    public CompletedOrderInformation(String custAddress, String custContact, String custImageUrl, String custUsername, String orderBill, String orderID) {
        this.custAddress = custAddress;
        this.custContact = custContact;
        this.custImageUrl = custImageUrl;
        this.custUsername = custUsername;
        this.orderBill = orderBill;
        this.orderID = orderID;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public String getCustContact() {
        return custContact;
    }

    public String getCustImageUrl() {
        return custImageUrl;
    }

    public String getCustUsername() {
        return custUsername;
    }

    public String getOrderBill() {
        return orderBill;
    }

    public String getOrderID() {
        return orderID;
    }
}