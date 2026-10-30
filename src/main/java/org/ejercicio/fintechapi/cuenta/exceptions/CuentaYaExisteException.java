package org.ejercicio.fintechapi.cuenta.exceptions;

public class CuentaYaExisteException extends RuntimeException {
    public CuentaYaExisteException(String message) {
        super(message);
    }
}
