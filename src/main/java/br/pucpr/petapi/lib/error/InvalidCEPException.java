package br.pucpr.petapi.lib.error;

public class InvalidCEPException extends RuntimeException{
    public InvalidCEPException(String message) {
        super(message);
    }
}
