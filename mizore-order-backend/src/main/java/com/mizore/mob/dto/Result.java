package com.mizore.mob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    /**
     * 1：成功
     * 0：失败
     */
    private Integer code;

    private String msg;

    private Object data;

    public static Result ok() {
        return new Result(1, "成功", null);
    }

    public static Result ok(Object data) {
        return new Result(1, "成功", data);
    }

    public static Result error(String msg) {
        return new Result(0, "Error: " + msg, null);
    }

}
