package io.github.ezequiellima10.fintech.account.controller;

import io.github.ezequiellima10.fintech.account.dto.AccountRequestDTO;
import io.github.ezequiellima10.fintech.account.dto.AccountRequestUpdateDTO;
import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.account.service.AccountService;
import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "Alta, consulta, modificación y baja de cuentas")
public class AccountController {


    private final AccountService accountService;


    @Operation(summary = "Busca una cuenta por id")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada")
    @ApiResponse(responseCode = "404", description = "No existe una cuenta con ese id")
    @GetMapping(path = "/api/accounts/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }


    @Operation(summary = "Crea una cuenta distinguiendo por tipo de cuenta")
    @ApiResponse(responseCode = "201", description = "Cuenta creada")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "404", description = "No existe el titular")
    @ApiResponse(responseCode = "409", description = "Ya existe una cuenta con ese alias")
    @PostMapping(path = "/api/accounts")
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO request) {
        AccountResponseDTO response = accountService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/accounts/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Busca todas las cuentas")
    @ApiResponse(responseCode = "200", description = "Cuentas encontradas (puede venir vacia)")
    @GetMapping(path = "/api/accounts")
    public ResponseEntity<ResponsePageDTO<AccountResponseDTO>> findAll(@ParameterObject Pageable page) {
        return ResponseEntity.ok(accountService.findAll(page));
    }


    @Operation(summary = "Actualiza el Alias de una cuenta")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada actualizada")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "404", description = "No existe una cuenta con ese id")
    @ApiResponse(responseCode = "409", description = "Ya existe una cuenta con ese alias")
    @PutMapping(path = "/api/accounts/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountRequestUpdateDTO request) {
        return ResponseEntity.ok(accountService.update(id, request));
    }


    @Operation(summary = "Elimina una cuenta por id")
    @ApiResponse(responseCode = "204", description = "Cuenta eliminada")
    @ApiResponse(responseCode = "404", description = "No existe una cuenta con ese id")
    @DeleteMapping(path = "/api/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
