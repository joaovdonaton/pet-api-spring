package br.pucpr.petapi.lib.error.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException{
    public BadRequestException(String message, String description) {
        super(message, description, HttpStatus.BAD_REQUEST);
    }
}
