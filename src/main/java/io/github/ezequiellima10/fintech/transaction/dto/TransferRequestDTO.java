package io.github.ezequiellima10.fintech.transaction.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequestDTO(

        @NotNull(message = "El cuenta de origen no puede ser nulo")
        @Positive(message = "El cuenta de origen tiene que ser positivo")
        Long originAccountId,

        @NotNull(message = "El cuenta de destino no puede ser nulo")
        @Positive(message = "El cuenta de destino tiene que ser positivo")
        Long destinationAccountId,

        @NotNull(message = "El monto no puede ser nulo")
        @Positive(message = "El monto debe ser positivo")
        @Digits(integer = 17, fraction = 2, message = "El monto admite hasta 17 enteros y 2 decimales")
        BigDecimal amount,

        @Size(max = 255, message = "La descripción no puede superar los {max} caracteres")
        String description
) {}
