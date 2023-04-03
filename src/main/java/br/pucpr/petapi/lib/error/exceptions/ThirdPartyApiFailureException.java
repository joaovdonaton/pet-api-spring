package br.pucpr.petapi.lib.error.exceptions;

import org.springframework.http.HttpStatus;

public class ThirdPartyApiFailureException extends ApiException{
    public ThirdPartyApiFailureException(String message, HttpStatus status) {
        super(message, status);
    }
}
