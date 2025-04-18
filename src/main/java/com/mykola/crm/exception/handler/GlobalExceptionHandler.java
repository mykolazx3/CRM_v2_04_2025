package com.mykola.crm.exception.handler;

import com.mykola.crm.exception.UserAlreadyExistsException;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //    @ExceptionHandler(MethodArgumentNotValidException.class)
    //    public ResponseEntity<String> handleValidationException(
    //    MethodArgumentNotValidException ex) {
    //        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
    //                .map(error -> error.getField() + ": " + error.getDefaultMessage())
    //                .collect(Collectors.joining(", "));
    //
    //        if (errorMessage.isEmpty()) {
    //            errorMessage = "Invalid input";
    //        }
    //
    //        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    //    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        if (errorMessage.isEmpty()) {
            errorMessage = "Invalid input";
        }

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    // Обробка виключення, якщо користувач вже існує
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Обробка виключення для невірних даних при аутентифікації
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
