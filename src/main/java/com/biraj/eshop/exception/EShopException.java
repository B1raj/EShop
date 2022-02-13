package com.biraj.eshop.exception;


import lombok.Data;

@Data
public abstract class EShopException extends RuntimeException  {

    private String errorCode;

    public EShopException(String errorCode, String errorDescription) {
        super(errorDescription);
        this.errorCode = errorCode;
    }


    public String getErrorCode() {
        return errorCode;

    }

}