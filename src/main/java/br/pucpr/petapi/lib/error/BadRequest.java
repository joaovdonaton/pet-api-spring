package br.pucpr.petapi.lib.error;

import org.springframework.http.HttpStatus;

public class BadRequest extends ApiException{
    public BadRequest(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
