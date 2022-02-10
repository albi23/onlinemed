package com.onlinemed.config;

import com.blueveery.core.model.BaseEntity;
import com.onlinemed.config.converters.CustomUUIDEditor;
import com.onlinemed.config.exceptions.TimeOutException;
import com.onlinemed.config.exceptions.ValidationException;
import com.onlinemed.model.dto.ErrorMessage;
import com.onlinemed.model.dto.Violation;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * The Global Controller Config class contains a common config for all controllers
 */
@ControllerAdvice
public class ControllersConfig {

    @InitBinder
    public void binder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new LocalDateTimeEditor());
        binder.registerCustomEditor(UUID.class, new CustomUUIDEditor());
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorMessage notFoundExceptionHandler(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return new ErrorMessage(ex.getMessage(), request.getRequestURL().toString());
    }

    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(TimeOutException.class)
    @ResponseBody
    public ErrorMessage timeOutExceptionHandler(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return new ErrorMessage(ex.getMessage(), request.getRequestURL().toString());
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ErrorMessage accessDeniedExceptionHandler(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return new ErrorMessage(ex.getMessage(), request.getRequestURL().toString());
    }


    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ErrorMessage badCredentialsExceptionHandler(Exception ex, HttpServletRequest request) {
        return new ErrorMessage(ex.getMessage(), request.getRequestURL().toString());
    }


    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(OptimisticLockException.class)
    @ResponseBody
    public ErrorMessage optimisticLockExceptionHandler(OptimisticLockException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return new ErrorMessage(ex.getMessage(), (BaseEntity) ex.getEntity());
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class, NumberFormatException.class})
    @ResponseBody
    public List<Violation> validationExceptionHandler(ValidationException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return ex.getViolationList();
    }


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorMessage exceptionHandler(Exception ex, HttpServletRequest request) {
        UUID errorID = UUID.randomUUID();
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        String stackTrace = stringWriter.getBuffer().toString();
        Timestamp errorTimeOccurrence = new Timestamp(new Date().getTime());
        System.err.println(String.format("[%s] Error id : %s, couse by:  \"%s\"\n%s", errorTimeOccurrence, errorID, ex.getMessage(), stackTrace));
        return new ErrorMessage(errorID, ex.getMessage(), stackTrace, request.getRequestURL().toString(), errorTimeOccurrence);
    }

    private static class LocalDateTimeEditor extends PropertyEditorSupport {

        public void setAsText(String value) {
            long timestamp = Long.parseLong(value);
            setValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
        }
    }
}
