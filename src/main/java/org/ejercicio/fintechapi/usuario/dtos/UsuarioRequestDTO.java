package org.ejercicio.fintechapi.usuario.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @NotBlank(message = "Nombre de usuario es obligatorio")
        @Size(max = 20, message = "El nombre no puede superar los {max} caracteres")
        String nombreUsuario,

        @NotBlank(message = "El Email es obligatorio")
        @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "El Email debe tener este formato nombre@dominio.com")
        @Size(max = 40, message = "El Email no puede superar los {max} caracteres")
        String email ) {}
