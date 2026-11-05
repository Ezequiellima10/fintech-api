package io.github.ezequiellima10.fintech.shared.exception;

import io.github.ezequiellima10.fintech.account.exception.AccountAlreadyExistsException;
import io.github.ezequiellima10.fintech.account.exception.AccountNotFoundException;
import io.github.ezequiellima10.fintech.exchangerate.exception.ExchangeRateUnavailableException;
import io.github.ezequiellima10.fintech.shared.dto.ExceptionResponseDTO;
import io.github.ezequiellima10.fintech.shared.dto.ExceptionResponseFieldsDTO;
import io.github.ezequiellima10.fintech.user.exception.UserAlreadyExistsException;
import io.github.ezequiellima10.fintech.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleUserNotFound(UserNotFoundException u){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.NOT_FOUND.value() , LocalDateTime.now(), u.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponseDto);
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleUserAlreadyExists(UserAlreadyExistsException u){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.CONFLICT.value() , LocalDateTime.now(), u.getMessage());
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDto);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAccountNotFound(AccountNotFoundException a){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.NOT_FOUND.value() , LocalDateTime.now(), a.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponseDto);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAccountAlreadyExists(AccountAlreadyExistsException a){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.CONFLICT.value() , LocalDateTime.now(), a.getMessage());
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDto);
    }

    @ExceptionHandler(ExchangeRateUnavailableException.class)
    public ResponseEntity<ExceptionResponseDTO> handleExchangeRateUnavailable(ExchangeRateUnavailableException e){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.SERVICE_UNAVAILABLE.value() , LocalDateTime.now(), e.getMessage());
        return  ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(exceptionResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseFieldsDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ExceptionResponseFieldsDTO exceptionResponseFieldsDTO = new ExceptionResponseFieldsDTO(LocalDateTime.now(), ex.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage()).toList());
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseFieldsDTO);
    }
}


