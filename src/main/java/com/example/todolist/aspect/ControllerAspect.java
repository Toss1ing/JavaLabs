package com.example.todolist.aspect;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ControllerAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.example.todolist.controller.*.*(..))")
    public void callMethodController(){
    }

    @After("callMethodController()")
    public void afterMethodCall(final JoinPoint jp){
        String args = Arrays.stream(jp.getArgs())
            .map(arg -> arg.toString())
            .collect(Collectors.joining(","));

        if(logger.isInfoEnabled()){
            logger.info("After(" + jp.getSignature().getName() + " args[" + args + "])");
        }
    }

    @Before("callMethodController()")
    public void beforeMethodCall(final JoinPoint jp){
        String args = Arrays.stream(jp.getArgs())
            .map(arg -> arg.toString())
            .collect(Collectors.joining(","));

        if(logger.isInfoEnabled()){
            logger.info("Before(" + jp.getSignature().getName() + " args[" + args + "])");
        }
    }

    @AfterReturning(value = "callMethodController()")
    public void afterMethodReturn(final JoinPoint jp){
        if(logger.isInfoEnabled()){
            logger.info("After returning(" + jp.getSignature().getName() + ")");
        }

    }

    @AfterThrowing(value = "callMethodController()", throwing = "ex")
    public void afterMethodThrow(final JoinPoint jp, Throwable ex){
        if(logger.isInfoEnabled()){
            logger.info("After Throwing(" + jp.getSignature().getName() + ")" + " exception(" + ex.getMessage() + ")");
        }
    }
}
