package br.pucpr.petapi.lib.error;

public class InvalidUUIDException extends RuntimeException{
    public InvalidUUIDException(String message) {
        super(message);
    }
}
