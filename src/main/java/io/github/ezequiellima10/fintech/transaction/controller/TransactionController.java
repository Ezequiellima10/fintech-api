package io.github.ezequiellima10.fintech.transaction.controller;

import io.github.ezequiellima10.fintech.transaction.dto.TransactionRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionResponseDTO;
import io.github.ezequiellima10.fintech.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Movimientos", description = "Depósito, Extracción y transferencias entre cuentas")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Realiza un Depósito")
    @ApiResponse(responseCode = "201", description = "Depósito confirmado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "No existe la cuenta")
    @PostMapping(path = "/api/accounts/{accountId}/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO, @PathVariable Long accountId) {
        TransactionResponseDTO responseDTO = transactionService.deposit(transactionRequestDTO, accountId);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/transactions/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(responseDTO);
    }


    @Operation(summary = "Realiza una Extracción")
    @ApiResponse(responseCode = "201", description = "Extracción confirmada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "No existe la cuenta")
    @ApiResponse(responseCode = "422", description = "Fondos insuficientes")
    @PostMapping(path = "/api/accounts/{accountId}/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO, @PathVariable Long accountId) {
        TransactionResponseDTO responseDTO = transactionService.withdraw(transactionRequestDTO, accountId);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/transactions/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(responseDTO);
    }
}
