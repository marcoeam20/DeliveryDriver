package com.example.hendryshanedeguia.driverretrieval;

/**
 * Created by HendryShanedeGuia on 11/09/2017.
 */

public class OrderInformation {
    public String custAddress;
    public String custContact;
    public String custImageUrl;
    public String custUsername;
    public String orderBill;

    public String getOrderGross() {
        return orderGross;
    }

    public void setOrderGross(String orderGross) {
        this.orderGross = orderGross;
    }

    public String getOrderVAT() {
        return orderVAT;
    }

    public void setOrderVAT(String orderVAT) {
        this.orderVAT = orderVAT;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String orderGross;
    public String orderVAT;
    public String orderID;
    public String promo;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String orderStatus;
    public OrderInformation() {
    }

    public OrderInformation(String custAddress, String custContact, String custImageUrl, String custUsername, String orderBill, String orderID) {
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
