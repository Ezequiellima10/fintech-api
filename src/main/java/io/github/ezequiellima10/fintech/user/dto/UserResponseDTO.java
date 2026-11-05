package io.github.ezequiellima10.fintech.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponseDTO (

        Long id,
        String username,
        String email,
        LocalDateTime createdAt ){}
