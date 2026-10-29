package org.ejercicio.fintechapi.usuario.service;

import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioRequestDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioResponseDTO;
import org.ejercicio.fintechapi.usuario.model.Usuario;
import org.springframework.data.domain.Pageable;


public interface UsuarioService {

    UsuarioResponseDTO create(UsuarioRequestDTO dto);

    ResponsePageDTO<UsuarioResponseDTO> findAll(Pageable page);

    UsuarioResponseDTO findByIdDto(Long id);

    Usuario findById(Long id);
}
