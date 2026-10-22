package org.ejercicio.fintechapi.usuario.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @NotBlank(message = "Nombre de usuario es obligatorio")
        @Size(max = 20)
        String nombreUsuario,

        @NotBlank(message = "El Email es obligatorio")
        @Email
        String email ) {}
