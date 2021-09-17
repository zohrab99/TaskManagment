package com.company.task.handler;

import com.company.task.exception.CustomAlreadyExistsException;
import com.company.task.exception.CustomNotFoundException;
import com.company.task.exception.CustomNotPermittedException;
import com.company.task.model.enums.Constant;
import com.company.task.model.view.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomNotFoundException.class})
    private ResponseEntity<Object> notFoundException(CustomNotFoundException exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        Response.builder()
                                .status(exc.getStatus())
                                .message(exc.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {CustomNotPermittedException.class})
    private ResponseEntity<Object> notPermitted(CustomNotPermittedException exc) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(
                        Response.builder()
                                .status(exc.getStatus())
                                .message(exc.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {CustomAlreadyExistsException.class})
    private ResponseEntity<Object> alreadyExist(CustomAlreadyExistsException exc) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        Response.builder()
                                .status(exc.getStatus())
                                .message(exc.getMessage())
                                .build()
                );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exc, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Response.builder()
                                .status(status.value())
                                .message(exc.getMostSpecificCause().getMessage())
                                .data(Constant.NOT_READABLE)
                                .build()
                );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exc, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(exc.getParameterName() + ":" + exc.getParameterType())
                                .data(Constant.MISSING_PARAMETER)
                                .build()
                );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exc, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final StringBuilder builder = new StringBuilder();
        exc.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    builder.append(fieldError.getField());
                    builder.append(":");
                    builder.append(fieldError.getDefaultMessage());
                    builder.append(";");
                });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(builder.toString())
                                .data(Constant.VALIDATION)
                                .build()
                );
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationExc(ConstraintViolationException exc) {
        StringBuffer buffer = new StringBuffer();
        exc.getConstraintViolations().forEach(v -> {
            buffer.append(v.getPropertyPath());
            buffer.append(":");
            buffer.append(v.getMessage());
            buffer.append(";");
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(buffer.toString())
                                .data(Constant.VALIDATION)
                                .build()
        );
    }


}
