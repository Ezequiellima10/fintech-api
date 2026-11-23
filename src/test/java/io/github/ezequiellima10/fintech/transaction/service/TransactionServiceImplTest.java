package io.github.ezequiellima10.fintech.transaction.service;

import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.account.exception.AccountNotFoundException;
import io.github.ezequiellima10.fintech.account.exception.InsufficientFundsException;
import io.github.ezequiellima10.fintech.account.service.AccountTransactionService;
import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionResponseDTO;
import io.github.ezequiellima10.fintech.transaction.mapper.TransactionMapper;
import io.github.ezequiellima10.fintech.transaction.model.Transaction;
import io.github.ezequiellima10.fintech.transaction.model.TransactionType;
import io.github.ezequiellima10.fintech.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountTransactionService accountTransactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    void deposit_validAmount_savesDepositTransaction() {
        // given
        BigDecimal amount = new BigDecimal("1000");
        TransactionRequestDTO request = new TransactionRequestDTO(amount, "sueldo");
        AccountResponseDTO account = AccountResponseDTO.builder().balance(new BigDecimal("1500")).build();
        TransactionResponseDTO expectedResponse = TransactionResponseDTO.builder().type(TransactionType.DEPOSIT).build();

        when(accountTransactionService.deposit(10L, amount)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionMapper.entityToDto(any(Transaction.class))).thenReturn(expectedResponse);

        // when
        TransactionResponseDTO result = transactionService.deposit(request, 10L);

        // then
        assertEquals(expectedResponse, result);

        // the captor "grabs" the transaction the service passed to save() so it can be inspected
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction savedTransaction = captor.getValue();

        assertEquals(10L, savedTransaction.getAccountId());
        assertEquals(TransactionType.DEPOSIT, savedTransaction.getType());
        assertEquals(amount, savedTransaction.getAmount());
        assertEquals(new BigDecimal("1500"), savedTransaction.getBalanceAfter());
        assertEquals("sueldo", savedTransaction.getDescription());
    }

    @Test
    void withdraw_validAmount_savesWithdrawalTransaction() {
        // given
        BigDecimal amount = new BigDecimal("400");
        TransactionRequestDTO request = new TransactionRequestDTO(amount, "alquiler");
        AccountResponseDTO account = AccountResponseDTO.builder().balance(new BigDecimal("600")).build();
        TransactionResponseDTO expectedResponse = TransactionResponseDTO.builder().type(TransactionType.WITHDRAWAL).build();

        when(accountTransactionService.withdraw(10L, amount)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionMapper.entityToDto(any(Transaction.class))).thenReturn(expectedResponse);

        // when
        TransactionResponseDTO result = transactionService.withdraw(request, 10L);

        // then
        assertEquals(expectedResponse, result);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction savedTransaction = captor.getValue();

        assertEquals(10L, savedTransaction.getAccountId());
        assertEquals(TransactionType.WITHDRAWAL, savedTransaction.getType());
        assertEquals(amount, savedTransaction.getAmount());
        assertEquals(new BigDecimal("600"), savedTransaction.getBalanceAfter());
    }

    @Test
    void withdraw_insufficientFunds_doesNotSaveTransaction() {
        // given
        BigDecimal amount = new BigDecimal("5000");
        TransactionRequestDTO request = new TransactionRequestDTO(amount, null);

        when(accountTransactionService.withdraw(10L, amount)).thenThrow(new InsufficientFundsException("Saldo insuficiente"));

        // when / then
        assertThrows(InsufficientFundsException.class, () -> transactionService.withdraw(request, 10L));

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void findTransactionHistory_existingAccount_mapsPageToDtos() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Transaction deposit = new Transaction(10L, TransactionType.DEPOSIT, new BigDecimal("1000"), new BigDecimal("1000"), null);
        Page<Transaction> page = new PageImpl<>(List.of(deposit), pageable, 1);
        TransactionResponseDTO depositDto = TransactionResponseDTO.builder().type(TransactionType.DEPOSIT).build();

        when(transactionRepository.findByAccountIdOrderByCreatedAtDesc(10L, pageable)).thenReturn(page);
        when(transactionMapper.entityToDto(deposit)).thenReturn(depositDto);

        // when
        ResponsePageDTO<TransactionResponseDTO> result = transactionService.findTransactionHistory(10L, pageable);

        // then
        assertEquals(List.of(depositDto), result.dtos());
        assertEquals(0, result.pageNumber());
        assertEquals(10, result.pageSize());
        assertEquals(1, result.totalElements());
        verify(accountTransactionService).existsAccount(10L);
    }

    @Test
    void findTransactionHistory_nonexistentAccount_throwsAccountNotFoundException() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        doThrow(new AccountNotFoundException("No existe la cuenta")).when(accountTransactionService).existsAccount(99L);

        // when / then
        assertThrows(AccountNotFoundException.class, () -> transactionService.findTransactionHistory(99L, pageable));

        verify(transactionRepository, never()).findByAccountIdOrderByCreatedAtDesc(any(), any());
    }
}
