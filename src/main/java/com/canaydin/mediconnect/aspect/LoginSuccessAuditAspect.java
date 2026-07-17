package com.canaydin.mediconnect.aspect;

import com.canaydin.mediconnect.security.auth.dto.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoginSuccessAuditAspect {

    @AfterReturning(
            pointcut = "execution(* com.canaydin.mediconnect.security.auth.service.AuthService.login(..))",
            returning = "response"
    )
    public void logSuccessfulLogin(Object response) {
        if (!(response instanceof AuthResponse authResponse)) {
            return;
        }

        log.info(
                "Login successful. userId={}, email={}, role={}",
                authResponse.getId(),
                authResponse.getEmail(),
                authResponse.getRole()
        );
    }
}