package com.canaydin.mediconnect.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionAuditAspect {

    @AfterThrowing(
            pointcut = "execution(* com.canaydin.mediconnect..service..*(..))",
            throwing = "ex"
    )
    public void logAfterException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();

        log.error(
                "Exception occurred in method: {}. exceptionType={}, message={}",
                methodName,
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
    }
}