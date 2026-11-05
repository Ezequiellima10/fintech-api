package io.github.ezequiellima10.fintech.exchangerate.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DolarApi responde con campos en español. @JsonAlias los acepta al leer
 * la respuesta, y al serializar se usan los nombres en inglés del record.
 */
public record UsdExchangeRateDTO(

        @JsonAlias("moneda") String currency,
        @JsonAlias("casa") String type,
        @JsonAlias("nombre") String name,
        @JsonAlias("compra") BigDecimal buy,
        @JsonAlias("venta") BigDecimal sell,
        @JsonAlias("fechaActualizacion") Instant updatedAt
) {
}
