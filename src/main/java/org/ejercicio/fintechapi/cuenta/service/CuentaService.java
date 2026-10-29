package org.ejercicio.fintechapi.cuenta.service;

import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaResponseDTO;
import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.springframework.data.domain.Pageable;

public interface CuentaService {

    CuentaResponseDTO create(CuentaRequestDTO cuentaRequestDTO);

    ResponsePageDTO<CuentaResponseDTO> findAll(Pageable pageable);

    CuentaResponseDTO findById(Long id);

    CuentaResponseDTO update(CuentaRequestDTO cuentaRequestDTO);

    void delete(Long id);
}
