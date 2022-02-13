package com.biraj.eshop.exception;

public class ProductNotFoundException extends EShopException{

    public ProductNotFoundException(){
        super("40000", "Product not found");
    }

    public ProductNotFoundException(String errorCode, String message){
        super(errorCode, message);
    }
}
