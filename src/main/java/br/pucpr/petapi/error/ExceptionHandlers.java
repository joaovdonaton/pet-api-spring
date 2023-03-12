package br.pucpr.petapi.error;

import br.pucpr.petapi.error.dto.ApiErrorDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ExceptionHandlers {
    // thrown by hibernate at jpa layer
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDTO> constraintViolation(ConstraintViolationException e){
        List<String> collect = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiErrorDTO(collect), BAD_REQUEST);
    }

    // thrown by service/controller layers
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> methodArgumentInvalid(MethodArgumentNotValidException e){
        var msg = e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiErrorDTO(msg), BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorDTO> usernameAlreadyExistsException(UsernameAlreadyExistsException e){
        return new ResponseEntity<>(new ApiErrorDTO(e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> usernameNotFound(UsernameNotFoundException e){
        return new ResponseEntity<>(new ApiErrorDTO(e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorDTO> invalidCredentials(InvalidCredentialsException e){
        return new ResponseEntity<>(new ApiErrorDTO(e.getMessage()), UNAUTHORIZED);
    }
}