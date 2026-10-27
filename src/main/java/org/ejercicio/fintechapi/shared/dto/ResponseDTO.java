package org.ejercicio.fintechapi.shared.dto;


import java.util.List;

public record ResponseDTO<T>(

        List<T> dtos,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages) {
}
