package org.ejercicio.fintechapi.usuario.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UsuarioResponseDTO (

        Long id,
        String nombreUsuario,
        String email,
        LocalDateTime fechaCreacion ){}
