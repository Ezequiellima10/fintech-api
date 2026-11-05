package io.github.ezequiellima10.fintech.shared.dto;

import java.time.LocalDateTime;

public record ExceptionResponseDTO(

        int statusCode,
        LocalDateTime dateTime,
        String errorMessage) {
}
