package org.ejercicio.fintechapi.cuenta.dtos;

import jakarta.validation.constraints.*;

import org.ejercicio.fintechapi.cuenta.model.TipoDeCuenta;

import java.math.BigDecimal;

public record CuentaRequestDTO(

        @NotNull(message = "El titularId no puede ser nulo")
        @Positive
        Long titularId,

        @NotBlank(message = "El Alias no puede quedar vacio")
        @Size(max= 20, message = "El maximo de caracteres para el alias es de {max}")
        String alias,

        @NotNull(message = "El tipoDeCuenta no puede ser nulo" )
        TipoDeCuenta tipoDeCuenta,

        @PositiveOrZero(message = "El limite descubierto debe ser mayor o igual a cero")
        BigDecimal limiteDescubierto
        ) {
}
