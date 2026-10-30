package org.ejercicio.fintechapi.cuenta.service;

import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestUpdateDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaResponseDTO;
import org.ejercicio.fintechapi.cuenta.exceptions.CuentaNoEncontradaException;
import org.ejercicio.fintechapi.cuenta.exceptions.CuentaYaExisteException;
import org.ejercicio.fintechapi.cuenta.mapper.CuentaMapper;
import org.ejercicio.fintechapi.cuenta.model.CajaAhorro;
import org.ejercicio.fintechapi.cuenta.model.Cuenta;
import org.ejercicio.fintechapi.cuenta.model.CuentaCorriente;
import org.ejercicio.fintechapi.cuenta.repository.CuentaRepository;
import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.ejercicio.fintechapi.usuario.model.Usuario;
import org.ejercicio.fintechapi.usuario.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;
    private final UsuarioService usuarioService;


    @Override
    @Transactional
    public CuentaResponseDTO create(CuentaRequestDTO cuentaRequestDTO) {

        if (cuentaRepository.existsByAlias(cuentaRequestDTO.alias())){
            throw new CuentaYaExisteException("Ya existe una cuenta con el alias " + cuentaRequestDTO.alias());
        }

        Usuario titular = usuarioService.findById(cuentaRequestDTO.titularId());

        Cuenta cuenta = switch (cuentaRequestDTO.tipoDeCuenta()) {
            case AHORRO -> new CajaAhorro(titular, cuentaRequestDTO.alias());
            case CORRIENTE -> new CuentaCorriente(titular, cuentaRequestDTO.alias(), cuentaRequestDTO.limiteDescubierto());
        };


        return cuentaMapper.entityToResponse(cuentaRepository.save(cuenta));
    }

    @Override
    public ResponsePageDTO<CuentaResponseDTO> findAll(Pageable page) {
        Page<Cuenta> pagina = cuentaRepository.findAll(page);
        Page<CuentaResponseDTO> paginaDtos = pagina.map(cuentaMapper::entityToResponse);
        return new ResponsePageDTO<>(paginaDtos.getContent(), paginaDtos.getNumber(), paginaDtos.getSize(), paginaDtos.getTotalElements(), paginaDtos.getTotalPages());
    }

    @Override
    public CuentaResponseDTO findById(Long id) {
        return cuentaMapper.entityToResponse((cuentaRepository.findById(id).orElseThrow(() -> new CuentaNoEncontradaException("No existe la cuenta con el id: " + id))));
    }


    @Override
    @Transactional
    public CuentaResponseDTO update(Long id, CuentaRequestUpdateDTO dto) {

        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNoEncontradaException("no existe una cuenta con el id: " + id));

        if (!dto.alias().equals(cuenta.getAlias()) && cuentaRepository.existsByAlias(dto.alias())) {
            throw new CuentaYaExisteException("Ya existe una cuenta con el alias " + dto.alias());
        }

        cuenta.setAlias(dto.alias());

        return cuentaMapper.entityToResponse(cuenta);
    }


    @Override
    @Transactional
    public void delete(Long id) {

        if (!cuentaRepository.existsById(id)) {
            throw new CuentaNoEncontradaException("No existe la cuenta con el id: " + id);
        }
        cuentaRepository.deleteById(id);
    }
}
