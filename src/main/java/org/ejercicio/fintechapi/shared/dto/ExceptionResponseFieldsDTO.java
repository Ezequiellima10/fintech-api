package org.ejercicio.fintechapi.shared.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponseFieldsDTO(

    LocalDateTime dateTime,
    List<String> errorMessages) {
}
