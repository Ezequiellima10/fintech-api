package org.ejercicio.fintechapi.cuenta.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestUpdateDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaResponseDTO;
import org.ejercicio.fintechapi.cuenta.service.CuentaService;
import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "Alta, consulta, modificación y baja de cuentas")
public class CuentaController {


    private final CuentaService cuentaService;


    @Operation(summary = "Busca una cuenta por id")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada")
    @ApiResponse(responseCode = "404", description = "No existe una cuenta con ese id")
    @GetMapping(path = "/cuentas/{id}")
    public ResponseEntity<CuentaResponseDTO> getCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.findById(id));
    }


    @Operation(summary = "Crea una cuenta distinguiendo por tipo de cuenta")
    @ApiResponse(responseCode = "201", description = "Cuenta creada")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "404", description = "No existe el titular")
    @ApiResponse(responseCode = "409", description = "Ya existe una cuenta con ese alias")
    @PostMapping(path = "/cuentas")
    public ResponseEntity<CuentaResponseDTO> createCuenta(@Valid @RequestBody CuentaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.create(request));
    }

    @Operation(summary = "Busca todas las cuentas")
    @ApiResponse(responseCode = "200", description = "Cuentas encontradas (puede venir vacia)")
    @GetMapping(path = "/cuentas")
    public ResponseEntity<ResponsePageDTO<CuentaResponseDTO>> findAll(@ParameterObject Pageable page) {
        return ResponseEntity.ok(cuentaService.findAll(page));
    }


    @Operation(summary = "Actualiza el Alias de una cuenta")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada actualizada")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "404", description = "No existe una cuenta con ese id")
    @ApiResponse(responseCode = "409", description = "Ya existe una cuenta con ese alias")
    @PutMapping(path = "/cuentas/{id}")
    public ResponseEntity<CuentaResponseDTO> updateCuenta(@PathVariable Long id, @Valid @RequestBody CuentaRequestUpdateDTO request) {
        return ResponseEntity.ok(cuentaService.update(id, request));
    }


    @Operation(summary = "Elimina una cuenta por id")
    @ApiResponse(responseCode = "204", description = "Cuenta eliminada")
    @ApiResponse(responseCode = "404", description = "No existe una cuenta con ese id")
    @DeleteMapping(path = "/cuentas/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
