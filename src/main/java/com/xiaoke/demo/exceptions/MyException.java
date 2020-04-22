package com.xiaoke.demo.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常类
 */
@Getter
@Setter
public class MyException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
	private Integer code;
    public MyException(String msg){
        super(msg);
    }

    public MyException(Integer code,String msg){
        super(msg);
        this.code=code;
    }
}