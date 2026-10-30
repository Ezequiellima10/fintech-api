package org.ejercicio.fintechapi.cotizacion.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record CotizacionDolarDTO(

        String moneda,
        String casa,
        String nombre,
        BigDecimal compra,
        BigDecimal venta,
        Instant fechaActualizacion
) {
}
