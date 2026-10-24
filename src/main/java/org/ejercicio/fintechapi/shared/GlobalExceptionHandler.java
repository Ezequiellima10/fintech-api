package org.ejercicio.fintechapi.shared;

import org.ejercicio.fintechapi.shared.dto.ExceptionResponseDTO;
import org.ejercicio.fintechapi.usuario.exceptions.UsuarioNoEncontradoException;
import org.ejercicio.fintechapi.usuario.exceptions.UsuarioYaExisteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
}


