package br.pucpr.petapi.lib.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class InvalidAddressException extends ApiException{
    public InvalidAddressException(String message, HttpStatus status) {
        super(message, status);
    }
}
