package br.pucpr.petapi.lib.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ResourceAlreadyExistsException extends ApiException{
    public ResourceAlreadyExistsException(String message, HttpStatus status) {
        super(message, status);
    }
}
