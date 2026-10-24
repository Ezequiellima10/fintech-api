package org.ejercicio.fintechapi.shared.dto;

import java.time.LocalDateTime;

public record ExceptionResponseDTO(

        int statusCode,
        LocalDateTime dateTime,
        String errorMessage) {
}
