package br.pucpr.petapi.lib.error;

public class InvalidAddressException extends RuntimeException{
    public InvalidAddressException(String message) {
        super(message);
    }
}
