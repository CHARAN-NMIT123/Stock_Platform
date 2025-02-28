package com.stocks.tradermanagement.aspect;
 
import java.util.Arrays;
 
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
 
    @Before("execution(* com.stocks.tradermanagement.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Executing method: " + joinPoint.getSignature());
        logger.info("Arguments: " + Arrays.toString(joinPoint.getArgs()));
    }
 
    @AfterReturning(pointcut = "execution(* com.stocks.tradermanagement.service.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method executed: " + joinPoint.getSignature().getName());
        try {
            logger.info("Method result: " + result);
        } catch (Error e) {
            logger.info(e.getMessage());
        }
    }
 
    @AfterThrowing(pointcut = "execution(* com.stocks.tradermanagement.service.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logger.error("Method threw an exception: " + joinPoint.getSignature().getName(), error);
    }
 
}