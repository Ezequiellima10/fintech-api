package io.github.ezequiellima10.fintech.transaction.service;

import io.github.ezequiellima10.fintech.transaction.dto.TransactionRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionResponseDTO;
import io.github.ezequiellima10.fintech.transaction.model.Transaction;

import java.math.BigDecimal;

public interface TransactionService {

    TransactionResponseDTO deposit(TransactionRequestDTO dto,  Long accountId);

    TransactionResponseDTO withdraw(TransactionRequestDTO dto, Long accountId);

}
