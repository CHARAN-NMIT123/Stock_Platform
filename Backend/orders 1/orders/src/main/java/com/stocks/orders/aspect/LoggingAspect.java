package com.stocks.orders.aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
 
@Aspect
@Component
public class LoggingAspect {
 
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
 
    @Before("execution(* com.stocks.orders.service.OrderServiceImpl.*(..))")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Method execution started: " + methodName);
    }
 
    @AfterReturning(pointcut = "execution(* com.stocks.orders.service.*.*(..))", returning = "result")
    public void afterMethodExecution(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Method execution completed: " + methodName);
        logger.info("Result: " + result);
    }
 
    @AfterThrowing(pointcut = "execution(* com.stocks.orders.service.OrderServiceImpl.*(..))", throwing = "exception")
    public void afterMethodThrowsException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("Method execution failed: " + methodName + " with exception: " + exception.getMessage());
    }
}