package com.htc.event.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    
    
    // Pointcut that matches all repositories, services and Web REST endpoints.
     
    @Before("execution(* com.htc.event.serviceimpl.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Before execution of: {}.{}() with arguments = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }
    
     
     // Advice that logs when a method is returned.
     
    @AfterReturning(
            pointcut = "execution(* com.htc.event.serviceimpl.*.*(..)) ", returning = "result" )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("After execution of: {}.{}() with result = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }
    
     
    // Advice that logs methods throwing exceptions.
      
    @AfterThrowing(
            pointcut = "execution(* com.htc.event.serviceimpl.*.*(..)) ", throwing = "error" )
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logger.error("Exception in {}.{}() with cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                error.getCause() != null ? error.getCause() : "NULL");
    }
    
    
     // Advice that logs when a method is entered and exited.
    
    @Around("execution(* com.htc.event.serviceimpl.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            logger.info("{}.{}() executed in {}ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    executionTime);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {} in {}.{}()",
                    joinPoint.getArgs(),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            throw e;
        }
    }
}

