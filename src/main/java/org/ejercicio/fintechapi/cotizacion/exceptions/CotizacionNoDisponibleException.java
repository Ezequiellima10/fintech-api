package org.ejercicio.fintechapi.cotizacion.exceptions;

public class CotizacionNoDisponibleException extends RuntimeException {
    public CotizacionNoDisponibleException(String message) {
        super(message);
    }

    public CotizacionNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
