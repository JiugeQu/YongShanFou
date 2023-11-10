package com.mizore.mob.config;

import com.mizore.mob.dto.Result;
import static com.mizore.mob.util.Constant.*;

import com.mizore.mob.exception.IdemPotentException;
import com.mizore.mob.exception.OrderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.toString());
        BindingResult bindingResult = e.getBindingResult();
        String errMsg = bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(" "));

        return Result.error(errMsg);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.toString());
        return Result.error("数据重复！！");
    }

    @ExceptionHandler({OrderException.class})
    @Order(value = 1)
    public Result handleOrderException(OrderException e) {
        log.error(e.toString());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(IdemPotentException.class)
    public Result handleExceptionHandler(IdemPotentException e) {
        log.error(e.toString());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @Order
    public Result handleRuntimeException(RuntimeException e) {
        log.error(e.toString());
        return Result.error(RUNTIME_EXCEPTION_MSG);
    }
}
