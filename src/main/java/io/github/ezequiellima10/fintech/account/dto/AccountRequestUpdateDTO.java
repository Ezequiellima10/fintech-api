package io.github.ezequiellima10.fintech.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccountRequestUpdateDTO(


        @NotBlank(message = "El Alias no puede quedar vacio")
        @Size(min = 5, max = 50, message = "El Alias debe tener un minimo de {min} caracteres y maximo de {max}")
        String alias
) {
}
