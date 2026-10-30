package org.ejercicio.fintechapi.cuenta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestUpdateDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaResponseDTO;
import org.ejercicio.fintechapi.cuenta.service.CuentaService;
import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CuentaController {


    private final CuentaService cuentaService;


    @GetMapping(path = "/cuentas/{id}")
    public ResponseEntity<CuentaResponseDTO> getCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.findById(id));
    }


    @PostMapping(path = "/cuentas")
    public ResponseEntity<CuentaResponseDTO> createCuenta(@Valid @RequestBody CuentaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.create(request));
    }

    @GetMapping(path = "/cuentas")
    public ResponseEntity<ResponsePageDTO<CuentaResponseDTO>> findAll(Pageable page) {
        return ResponseEntity.ok(cuentaService.findAll(page));
    }


    @PutMapping(path = "/cuentas/{id}")
    public ResponseEntity<CuentaResponseDTO> updateCuenta(@PathVariable Long id, @Valid @RequestBody CuentaRequestUpdateDTO request) {
        return ResponseEntity.ok(cuentaService.update(id, request));
    }


    @DeleteMapping(path = "/cuentas/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
