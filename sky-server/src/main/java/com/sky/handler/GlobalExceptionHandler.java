package com.sky.handler;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
    @ExceptionHandler(SQLException.class)
    public Result SQLExceptionHandler(SQLException ex){
        log.error("数据库异常信息：{}", ex.getMessage());
        String message = ex.getMessage();

        if(message.contains("Duplicate entry") && message.contains(JwtClaimsConstant.USERNAME)) {
            log.error("用户名已存在");
            return Result.error(MessageConstant.USERNAME_EXIST);
        }
        return Result.error(ex.getMessage());
    }
    @ExceptionHandler(HttpClientErrorException.class)
    public Result HttpClientErrorExceptionHandler(HttpClientErrorException ex) {
        log.error(ex.getMessage());
        return Result.error(ex.getMessage());
    }

}
