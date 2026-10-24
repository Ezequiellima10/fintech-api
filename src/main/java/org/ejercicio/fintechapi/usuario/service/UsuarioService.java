package org.ejercicio.fintechapi.usuario.service;

import org.ejercicio.fintechapi.usuario.dtos.UsuarioRequestDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioResponseDTO;
import org.ejercicio.fintechapi.usuario.model.Usuario;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO create(UsuarioRequestDTO dto);

    List<UsuarioResponseDTO> findAll();

    UsuarioResponseDTO findByIdDto(Long id);

    Usuario findById(Long id);
}
