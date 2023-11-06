package com.mizore.mob.exception;

public class IdemPotentException extends RuntimeException{

    public IdemPotentException(String msg) {
        super(msg);
    }

    public IdemPotentException(String msg, Throwable e) {
        super(msg, e);
    }
}
