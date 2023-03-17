package br.pucpr.petapi.lib.error;

import org.springframework.http.HttpStatus;

public class ThirdPartyApiFailureException extends ApiException{
    public ThirdPartyApiFailureException(String message, HttpStatus status) {
        super(message, status);
    }
}
