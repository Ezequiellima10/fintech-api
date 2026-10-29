package org.ejercicio.fintechapi.cuenta.dtos;

import lombok.Builder;
import org.ejercicio.fintechapi.cuenta.model.TipoDeCuenta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CuentaResponseDTO(

        Long id,
        BigDecimal saldo,
        Long titularId,
        String alias,
        TipoDeCuenta tipoDeCuenta,
        LocalDateTime fechaCreacion,
        BigDecimal limiteDescubierto
) {
}
