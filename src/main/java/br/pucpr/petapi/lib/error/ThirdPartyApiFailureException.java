package br.pucpr.petapi.lib.error;

public class ThirdPartyApiFailureException extends RuntimeException{
    public ThirdPartyApiFailureException(String message) {
        super(message);
    }
}
