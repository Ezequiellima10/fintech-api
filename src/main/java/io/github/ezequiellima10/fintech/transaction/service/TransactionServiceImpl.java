package io.github.ezequiellima10.fintech.transaction.service;

import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.account.service.AccountTransactionService;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionResponseDTO;
import io.github.ezequiellima10.fintech.transaction.mapper.TransactionMapper;
import io.github.ezequiellima10.fintech.transaction.model.Transaction;
import io.github.ezequiellima10.fintech.transaction.model.TransactionType;
import io.github.ezequiellima10.fintech.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountTransactionService accountTransactionService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    @Override
    public TransactionResponseDTO deposit(TransactionRequestDTO dto, Long accountId) {

           AccountResponseDTO accountdto = accountTransactionService.deposit(accountId, dto.amount());

            Transaction transaction = transactionRepository.save(new Transaction(accountId, TransactionType.DEPOSIT, dto.amount(), accountdto.balance(), dto.description()));

        return transactionMapper.entityToDto(transaction);
    }

    @Transactional
    @Override
    public TransactionResponseDTO withdraw(TransactionRequestDTO dto, Long accountId) {
        AccountResponseDTO accountResponseDTO = accountTransactionService.withdraw(accountId, dto.amount());

        Transaction transaction = transactionRepository.save(new Transaction(accountId, TransactionType.WITHDRAWAL, dto.amount(), accountResponseDTO.balance(), dto.description()));

        return transactionMapper.entityToDto(transaction);
    }
}
