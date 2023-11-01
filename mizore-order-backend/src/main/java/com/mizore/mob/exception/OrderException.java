package com.mizore.mob.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


public class OrderException extends RuntimeException{


    public OrderException(String msg) {
        super(msg);
    }

    public OrderException(String msg, Exception e) {
        super(msg, e);
    }
}
