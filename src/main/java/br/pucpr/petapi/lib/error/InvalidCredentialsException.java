package br.pucpr.petapi.lib.error;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException(String message, HttpStatus status) {
        super(message, status);
    }
}
