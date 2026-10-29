package org.ejercicio.fintechapi.cuenta.service;

import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaResponseDTO;
import org.ejercicio.fintechapi.cuenta.mapper.CuentaMapper;
import org.ejercicio.fintechapi.cuenta.model.Cuenta;
import org.ejercicio.fintechapi.cuenta.repository.CuentaRepository;
import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Override
    public CuentaResponseDTO create(CuentaRequestDTO cuentaRequestDTO) {
        return null;
    }

    @Override
    public ResponsePageDTO<CuentaResponseDTO> findAll(Pageable page) {
        Page<Cuenta> pagina = cuentaRepository.findAll(page);
        Page<CuentaResponseDTO> paginaDtos = pagina.map(cuentaMapper::entityToResponse);
        return new ResponsePageDTO<>(paginaDtos.getContent(), paginaDtos.getNumber(), paginaDtos.getSize(), paginaDtos.getTotalElements(), paginaDtos.getTotalPages());
    }

    @Override
    public CuentaResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public CuentaResponseDTO update(CuentaRequestDTO cuentaRequestDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
