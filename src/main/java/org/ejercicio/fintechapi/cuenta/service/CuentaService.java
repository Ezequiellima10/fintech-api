package org.ejercicio.fintechapi.cuenta.service;

import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestUpdateDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaResponseDTO;
import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.springframework.data.domain.Pageable;

public interface CuentaService {

    CuentaResponseDTO create(CuentaRequestDTO cuentaRequestDTO);

    ResponsePageDTO<CuentaResponseDTO> findAll(Pageable pageable);

    CuentaResponseDTO findById(Long id);

    CuentaResponseDTO update(Long id, CuentaRequestUpdateDTO cuentaRequestUpdateDTO);

    void delete(Long id);
}
