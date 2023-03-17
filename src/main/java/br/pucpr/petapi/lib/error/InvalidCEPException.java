package br.pucpr.petapi.lib.error;

import org.springframework.http.HttpStatus;

public class InvalidCEPException extends ApiException{
    public InvalidCEPException(String message, HttpStatus status) {
        super(message, status);
    }
}
