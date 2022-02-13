package com.biraj.eshop.exception;

public class OrderNotFoundException extends EShopException{

    public OrderNotFoundException(){
        super("40002", "Order not found");
    }


}
