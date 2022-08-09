package com.itheima.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-05 12:35
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            return R.error(s[2]+"account name already exists !");
        }

        return R.error("error !");
    }

    @ExceptionHandler(CustomerException.class)
    public R<String> exceptionHandler(CustomerException ex){
        log.error(ex.getMessage());
    //    if (ex.getMessage().contains("Duplicate entry")){
    //        String[] s = ex.getMessage().split(" ");
    //        return R.error(s[2]+"account name already exists !");
    //}

        return R.error(ex.getMessage());
    }

}
