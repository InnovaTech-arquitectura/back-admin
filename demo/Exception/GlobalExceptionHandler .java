package com.innovatech.demo.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Adjust the status code as needed
    public void handleException(HttpServletRequest request, Exception ex) {
        logger.error("Exception caught: {}", ex.getMessage());
        logger.error("Request Method: {}", request.getMethod());
        logger.error("Request URI: {}", request.getRequestURI());
        logger.error("Request Headers: {}", getHeaders(request));
        logger.error("Stack Trace: ", ex);
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            headers.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
        });
        return headers.toString();
    }
}
