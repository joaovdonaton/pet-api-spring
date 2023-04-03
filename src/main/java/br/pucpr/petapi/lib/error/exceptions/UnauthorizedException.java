package br.pucpr.petapi.lib.error.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException{
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
