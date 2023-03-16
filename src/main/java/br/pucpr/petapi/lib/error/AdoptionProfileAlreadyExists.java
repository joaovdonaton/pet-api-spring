package br.pucpr.petapi.lib.error;

public class AdoptionProfileAlreadyExists extends RuntimeException{
    public AdoptionProfileAlreadyExists(String message) {
        super(message);
    }
}
