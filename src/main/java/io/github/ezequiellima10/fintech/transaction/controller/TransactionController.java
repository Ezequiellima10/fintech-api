package io.github.ezequiellima10.fintech.transaction.controller;

import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionResponseDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransferRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransferResponseDTO;
import io.github.ezequiellima10.fintech.transaction.service.TransactionService;
import io.github.ezequiellima10.fintech.transaction.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestController
@RequiredArgsConstructor
@Tag(name = "Movimientos", description = "Depósito, Extracción y transferencias entre cuentas")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransferService transferService;

    @Operation(summary = "Realiza un Depósito")
    @ApiResponse(responseCode = "201", description = "Depósito confirmado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "No existe la cuenta")
    @ApiResponse(responseCode = "409", description = "La cuenta está cerrada")
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
    @ApiResponse(responseCode = "409", description = "La cuenta está cerrada")
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

    @Operation(summary = "Transfiere un monto de una cuenta a otra")
    @ApiResponse(responseCode = "201", description = "Transferencia confirmada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "No existe la cuenta")
    @ApiResponse(responseCode = "409", description = "La cuenta está cerrada")
    @ApiResponse(responseCode = "422", description = "Fondos insuficientes o misma cuenta de origen y destino")
    @PostMapping(path = "/api/transfers")
    public ResponseEntity<TransferResponseDTO> transfer(@Valid @RequestBody TransferRequestDTO transferDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferService.transfer(transferDto));
    }

    @Operation(summary = "Busca los movimientos de una cuenta, del más reciente al más antiguo")
    @ApiResponse(responseCode = "200", description = "Movimientos encontrados (puede venir vacío)")
    @ApiResponse(responseCode = "404", description = "No existe la cuenta")
    @GetMapping(path = "/api/accounts/{accountId}/transactions")
    public ResponseEntity<ResponsePageDTO<TransactionResponseDTO>> findTransactionHistory(@PathVariable Long accountId, @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(transactionService.findTransactionHistory(accountId, pageable));
    }

    @Operation(summary = "Busca un movimiento por id")
    @ApiResponse(responseCode = "200", description = "Movimiento encontrado")
    @ApiResponse(responseCode = "404", description = "No existe el movimiento")
    @GetMapping(path = "/api/transactions/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }
}
