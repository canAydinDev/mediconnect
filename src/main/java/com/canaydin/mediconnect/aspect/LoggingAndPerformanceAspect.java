package com.canaydin.mediconnect.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAndPerformanceAspect {

    @Around("execution(* com.canaydin.mediconnect..service..*(..))")
    public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().toShortString();

        log.debug("Entering method: {}", methodName);

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - startTime;

        log.debug("Exiting method: {}. Execution time: {} ms", methodName, executionTime);

        return result;
    }
}