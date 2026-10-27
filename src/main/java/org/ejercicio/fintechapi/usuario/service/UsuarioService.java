package org.ejercicio.fintechapi.usuario.service;

import org.ejercicio.fintechapi.shared.dto.ResponseDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioRequestDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioResponseDTO;
import org.ejercicio.fintechapi.usuario.model.Usuario;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO create(UsuarioRequestDTO dto);

    ResponseDTO<UsuarioResponseDTO> findAll(Pageable page);

    UsuarioResponseDTO findByIdDto(Long id);

    Usuario findById(Long id);
}
