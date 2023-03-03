package com.event.utils;

import com.event.common.Result;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //必要2
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

}
