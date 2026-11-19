package io.github.ezequiellima10.fintech.transaction.mapper;

import io.github.ezequiellima10.fintech.transaction.dto.TransferResponseDTO;
import io.github.ezequiellima10.fintech.transaction.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    public TransferResponseDTO entityToDto(Transaction transferOut, Transaction transferIn) {
        return TransferResponseDTO.builder()
                .originAccountId(transferOut.getAccountId())
                .destinationAccountId(transferIn.getAccountId())
                .amount(transferOut.getAmount())
                .description(transferOut.getDescription())
                .build();
    }
}
