package io.github.ezequiellima10.fintech.account.dto;

import io.github.ezequiellima10.fintech.account.model.AccountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AccountRequestDTO(

        @NotNull(message = "El holderId no puede ser nulo")
        @Positive
        Long holderId,

        @NotBlank(message = "El Alias no puede quedar vacio")
        @Size(min = 5, max = 50, message = "El Alias debe tener un minimo de {min} caracteres y maximo de {max}")
        String alias,

        @NotNull(message = "El accountType no puede ser nulo" )
        AccountType accountType,

        @PositiveOrZero(message = "El limite descubierto debe ser mayor o igual a cero")
        BigDecimal overdraftLimit
        ) {
}
