package io.github.ezequiellima10.fintech.transaction.controller;

import io.github.ezequiellima10.fintech.account.exception.InsufficientFundsException;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionResponseDTO;
import io.github.ezequiellima10.fintech.transaction.model.TransactionType;
import io.github.ezequiellima10.fintech.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest loads only the web layer: controller, Bean Validation and the @RestControllerAdvice (no JPA, no DB)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;


    @Test
    void deposit_validRequest_returns201WithLocation() throws Exception {
        // given
        TransactionResponseDTO response = TransactionResponseDTO.builder()
                .id(1L)
                .accountId(10L)
                .type(TransactionType.DEPOSIT)
                .amount(new BigDecimal("1000"))
                .build();

        when(transactionService.deposit(any(TransactionRequestDTO.class), eq(10L))).thenReturn(response);

        // when / then
        mockMvc.perform(post("/api/accounts/10/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount": 1000, "description": "sueldo"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/transactions/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("DEPOSIT"));
    }

    @Test
    void deposit_negativeAmount_returns400() throws Exception {
        // when / then
        mockMvc.perform(post("/api/accounts/10/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount": -50}
                                """))
                .andExpect(status().isBadRequest());

        // @Valid rejects the request before it reaches the service
        verifyNoInteractions(transactionService);
    }

    @Test
    void withdraw_insufficientFunds_returns422() throws Exception {
        // given
        when(transactionService.withdraw(any(TransactionRequestDTO.class), eq(10L)))
                .thenThrow(new InsufficientFundsException("Saldo insuficiente"));

        // when / then
        mockMvc.perform(post("/api/accounts/10/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount": 5000}
                                """))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.errorMessage").value("Saldo insuficiente"));
    }
}
