package com.onlinemed.cfg;

import com.onlinemed.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    ProblemDetail handle(ValidationException validationEx, HttpServletRequest httpReq) {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setDetail(validationEx.getLocalizedMessage());
        problemDetail.setProperty("error-message", validationEx.getViolationList().toString());
        problemDetail.setProperty("request-uri", httpReq.getRequestURI());
        return problemDetail;
    }
}
