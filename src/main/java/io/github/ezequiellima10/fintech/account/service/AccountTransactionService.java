package io.github.ezequiellima10.fintech.account.service;

import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;

import java.math.BigDecimal;

public interface AccountTransactionService {

    AccountResponseDTO deposit(Long accountId, BigDecimal amount);
    AccountResponseDTO withdraw(Long accountId, BigDecimal amount);
}
