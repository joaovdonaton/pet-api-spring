package br.pucpr.petapi.lib.error.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidAddressException extends ApiException{
    public InvalidAddressException(String message, HttpStatus status) {
        super(message, status);
    }
}
