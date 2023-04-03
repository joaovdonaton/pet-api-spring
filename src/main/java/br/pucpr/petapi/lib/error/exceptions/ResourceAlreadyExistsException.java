package br.pucpr.petapi.lib.error.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends ApiException{
    public ResourceAlreadyExistsException(String message, String details, HttpStatus status) {
        super(message, details, status);
    }
}
