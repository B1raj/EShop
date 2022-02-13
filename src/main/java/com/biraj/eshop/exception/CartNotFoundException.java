package com.biraj.eshop.exception;

public class CartNotFoundException extends EShopException{

    public CartNotFoundException(){
        super("40001", "Cart not found");
    }


}
