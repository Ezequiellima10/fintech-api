package io.github.ezequiellima10.fintech.account.dto;

public record TransferBalancesDTO(

        AccountResponseDTO origin,
        AccountResponseDTO destination
) {
}
