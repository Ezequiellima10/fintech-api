package org.ejercicio.fintechapi.cuenta.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CuentaRequestUpdateDTO(


        @NotBlank(message = "El Alias no puede quedar vacio")
        @Size(min = 5, max = 20, message = "El Alias debe tener un minimo de {min} caracteres y maximo de {max}")
        String alias
) {
}
