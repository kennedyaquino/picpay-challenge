package com.kennedy.picpay.controllers;

import com.kennedy.picpay.exception.PicpayException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(PicpayException.class)
    public ProblemDetail handlePicPayException(PicpayException e) {
        return e.toProblemDetail();
    }
}
