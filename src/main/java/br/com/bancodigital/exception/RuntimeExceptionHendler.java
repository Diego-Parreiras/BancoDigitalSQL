package br.com.bancodigital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RuntimeExceptionHendler {

    @ExceptionHandler(JavaException.class)
    public ResponseEntity<String> handler(JavaException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());

    }
}
