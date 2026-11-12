package io.github.ezequiellima10.fintech.transaction.mapper;

import io.github.ezequiellima10.fintech.transaction.dto.TransactionRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionResponseDTO;
import io.github.ezequiellima10.fintech.transaction.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDTO entityToDto(Transaction entity) {
        return TransactionResponseDTO.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .type(entity.getType())
                .amount(entity.getAmount())
                .balanceAfter(entity.getBalanceAfter())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
