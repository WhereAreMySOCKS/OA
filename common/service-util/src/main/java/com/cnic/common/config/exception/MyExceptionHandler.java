package com.cnic.common.config.exception;

import com.cnic.common.result.ResultCodeEnum;
import lombok.Data;


@Data
public class MyExceptionHandler extends RuntimeException {

    private Integer code;
    private String msg;

    public MyExceptionHandler(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public MyExceptionHandler(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "GuiguException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
