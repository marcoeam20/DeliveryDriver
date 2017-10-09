package com.example.hendryshanedeguia.driverretrieval;

/**
 * Created by macbook on 01/10/2017.
 */

public class OrderSumModel {

    String orderQuantity;
    String prodName;
    String prodPrice;
    String subTotal;

    private OrderSumModel (){

    }
    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }



}
