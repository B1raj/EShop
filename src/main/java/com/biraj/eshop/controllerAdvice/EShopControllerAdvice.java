package com.biraj.eshop.controllerAdvice;

import com.biraj.eshop.beans.ErrorInfo;
import com.biraj.eshop.exception.CartNotFoundException;
import com.biraj.eshop.exception.OrderNotFoundException;
import com.biraj.eshop.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = { "com.biraj.eshop.controller" })
@Slf4j
public class EShopControllerAdvice {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseBody
    ErrorInfo handleProductNotFoundException(HttpServletRequest req, ProductNotFoundException exception) {
        log.error("BadRequest", exception);
        return createErrorResponse(exception.getMessage(), exception.getErrorCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CartNotFoundException.class)
    @ResponseBody
    ErrorInfo handleCartNotFoundException(HttpServletRequest req, CartNotFoundException exception) {
        log.error("BadRequest", exception);
        return createErrorResponse(exception.getMessage(), exception.getErrorCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseBody
    ErrorInfo handleOrderNotFoundException(HttpServletRequest req, OrderNotFoundException exception) {
        log.error("BadRequest", exception);
        return createErrorResponse(exception.getMessage(), exception.getErrorCode());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ErrorInfo handleUnhandledException(HttpServletRequest req, Throwable exception) {
        log.error("UnhandledException", exception);
        return createErrorResponse("Unexpected error occurred. Please contact the Administrator", "500");
    }

    private ErrorInfo createErrorResponse( String errorMessage, String errorCode) {
        ErrorInfo errorInfo = null;
        try {
            errorInfo = new ErrorInfo(errorCode, errorMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return errorInfo;
    }
}
