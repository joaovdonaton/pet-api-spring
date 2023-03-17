package br.pucpr.petapi.lib.error;

import org.springframework.http.HttpStatusCode;

public class AdoptionProfileAlreadyExistsException extends ApiException{
    public AdoptionProfileAlreadyExistsException(String message, HttpStatusCode status) {
        super(message, status);
    }
}
