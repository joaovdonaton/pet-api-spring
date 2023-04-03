package br.pucpr.petapi.lib.error.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ApiException extends RuntimeException{
    private HttpStatus status;
    private String details = "";
    public ApiException(String message, String details, HttpStatus status) {
        super(message);
        this.status = status;
        this.details = details;
    }

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
