package br.pucpr.petapi.lib.error.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceDoesNotExistException extends ApiException{
    public ResourceDoesNotExistException(String message, HttpStatus status) {
        super(message, status);
    }
}
