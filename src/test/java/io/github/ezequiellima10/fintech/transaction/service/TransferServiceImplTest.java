package io.github.ezequiellima10.fintech.transaction.service;

import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.account.dto.TransferBalancesDTO;
import io.github.ezequiellima10.fintech.account.service.AccountTransactionService;
import io.github.ezequiellima10.fintech.transaction.dto.TransferRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransferResponseDTO;
import io.github.ezequiellima10.fintech.transaction.exception.SameAccountTransferException;
import io.github.ezequiellima10.fintech.transaction.mapper.TransferMapper;
import io.github.ezequiellima10.fintech.transaction.model.Transaction;
import io.github.ezequiellima10.fintech.transaction.model.TransactionType;
import io.github.ezequiellima10.fintech.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountTransactionService accountTransactionService;

    @Mock
    private TransferMapper transferMapper;

    @InjectMocks
    private TransferServiceImpl transferService;


    @Test
    void transfer_validRequest_savesTransferOutAndTransferIn() {
        // given
        BigDecimal amount = new BigDecimal("400");
        TransferRequestDTO request = new TransferRequestDTO(10L, 20L, amount, "alquiler");
        TransferBalancesDTO balances = new TransferBalancesDTO(
                AccountResponseDTO.builder().balance(new BigDecimal("600")).build(),
                AccountResponseDTO.builder().balance(new BigDecimal("1400")).build());
        TransferResponseDTO expectedResponse = TransferResponseDTO.builder().originAccountId(10L).destinationAccountId(20L).build();

        when(accountTransactionService.transfer(10L, 20L, amount)).thenReturn(balances);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transferMapper.entityToDto(any(Transaction.class), any(Transaction.class))).thenReturn(expectedResponse);

        // when
        TransferResponseDTO result = transferService.transfer(request);

        // then
        assertEquals(expectedResponse, result);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(2)).save(captor.capture());
        Transaction out = captor.getAllValues().get(0);
        Transaction in = captor.getAllValues().get(1);

        assertEquals(10L, out.getAccountId());
        assertEquals(TransactionType.TRANSFER_OUT, out.getType());
        assertEquals(new BigDecimal("600"), out.getBalanceAfter());

        assertEquals(20L, in.getAccountId());
        assertEquals(TransactionType.TRANSFER_IN, in.getType());
        assertEquals(new BigDecimal("1400"), in.getBalanceAfter());

        // the mapper must receive (out, in) in this order, otherwise origin and destination come out swapped
        verify(transferMapper).entityToDto(out, in);
    }

    @Test
    void transfer_sameAccount_throwsAndDoesNotMoveFunds() {
        // given
        TransferRequestDTO request = new TransferRequestDTO(10L, 10L, new BigDecimal("400"), null);

        // when / then
        assertThrows(SameAccountTransferException.class, () -> transferService.transfer(request));

        verifyNoInteractions(accountTransactionService, transactionRepository);
    }
}
