package io.github.ezequiellima10.fintech.transaction.service;

import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionResponseDTO;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    TransactionResponseDTO deposit(TransactionRequestDTO dto,  Long accountId);

    TransactionResponseDTO withdraw(TransactionRequestDTO dto, Long accountId);

    ResponsePageDTO<TransactionResponseDTO> findTransactionHistory(Long accountId, Pageable pageable);
}
