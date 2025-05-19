package br.com.bancodigital.exception;

import lombok.Data;

@Data
public class JavaException extends RuntimeException {
    public JavaException(String message) {
        super(message);
    }

    private int statusCode;
    public JavaException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
