package br.pucpr.petapi.lib.error.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends ApiException{
    public ResourceAlreadyExistsException(String message, HttpStatus status) {
        super(message, status);
    }
}
