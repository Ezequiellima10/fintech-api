package io.github.ezequiellima10.fintech.account.dto;

import io.github.ezequiellima10.fintech.account.model.AccountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record AccountResponseDTO(

        Long id,
        BigDecimal balance,
        Long holderId,
        String alias,
        AccountType accountType,
        LocalDateTime createdAt,
        BigDecimal overdraftLimit
) {
}
