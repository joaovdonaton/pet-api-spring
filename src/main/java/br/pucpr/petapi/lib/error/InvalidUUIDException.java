package br.pucpr.petapi.lib.error;

import org.springframework.http.HttpStatus;

public class InvalidUUIDException extends ApiException{
    public InvalidUUIDException(String message, HttpStatus status) {
        super(message, status);
    }
}
