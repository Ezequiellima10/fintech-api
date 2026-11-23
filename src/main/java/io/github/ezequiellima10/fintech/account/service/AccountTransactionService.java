package io.github.ezequiellima10.fintech.account.service;

import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.account.dto.TransferBalancesDTO;

import java.math.BigDecimal;

public interface AccountTransactionService {

    AccountResponseDTO deposit(Long accountId, BigDecimal amount);
    AccountResponseDTO withdraw(Long accountId, BigDecimal amount);
    void existsAccount(Long accountId);
    TransferBalancesDTO transfer(Long originId, Long destinationId, BigDecimal amount);
}
