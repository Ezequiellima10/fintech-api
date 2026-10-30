package org.ejercicio.fintechapi.shared;

import org.ejercicio.fintechapi.cotizacion.exceptions.CotizacionNoDisponibleException;
import org.ejercicio.fintechapi.cuenta.exceptions.CuentaNoEncontradaException;
import org.ejercicio.fintechapi.cuenta.exceptions.CuentaYaExisteException;
import org.ejercicio.fintechapi.shared.dto.ExceptionResponseDTO;
import org.ejercicio.fintechapi.shared.dto.ExceptionResponseFieldsDTO;
import org.ejercicio.fintechapi.usuario.exceptions.UsuarioNoEncontradoException;
import org.ejercicio.fintechapi.usuario.exceptions.UsuarioYaExisteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ExceptionResponseDTO> handleUsuarioNoEncontrado(UsuarioNoEncontradoException u){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.NOT_FOUND.value() , LocalDateTime.now(), u.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponseDto);
    }


    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<ExceptionResponseDTO> handleUsuarioDuplicado(UsuarioYaExisteException u){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.CONFLICT.value() , LocalDateTime.now(), u.getMessage());
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDto);
    }

    @ExceptionHandler(CuentaNoEncontradaException.class)
    public ResponseEntity<ExceptionResponseDTO> handleCuentaNoEncontrada(CuentaNoEncontradaException c){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.NOT_FOUND.value() , LocalDateTime.now(), c.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponseDto);
    }

    @ExceptionHandler(CuentaYaExisteException.class)
    public ResponseEntity<ExceptionResponseDTO> handleCuentaDuplicada(CuentaYaExisteException c){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.CONFLICT.value() , LocalDateTime.now(), c.getMessage());
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDto);
    }

    @ExceptionHandler(CotizacionNoDisponibleException.class)
    public ResponseEntity<ExceptionResponseDTO> handleCotizacionNoDisponible(CotizacionNoDisponibleException c){
        ExceptionResponseDTO exceptionResponseDto = new ExceptionResponseDTO(HttpStatus.SERVICE_UNAVAILABLE.value() , LocalDateTime.now(), c.getMessage());
        return  ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(exceptionResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseFieldsDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ExceptionResponseFieldsDTO exceptionResponseFieldsDTO = new ExceptionResponseFieldsDTO(LocalDateTime.now(), ex.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage()).toList());
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseFieldsDTO);
    }
}


