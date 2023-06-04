package com.cnic.common.config.exception;

import com.cnic.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常处理方法
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(){
        return Result.fail().message("全局异常处理");
    }

    //特定异常ArithmeticException处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("特定异常处理");
    }


}
