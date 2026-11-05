package io.github.ezequiellima10.fintech.shared.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponseFieldsDTO(

    LocalDateTime dateTime,
    List<String> errorMessages) {
}
