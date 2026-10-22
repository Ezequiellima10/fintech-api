package org.ejercicio.fintechapi.usuario.service;

import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioRequestDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioResponseDTO;
import org.ejercicio.fintechapi.usuario.exceptions.UsuarioNoEncontradoException;
import org.ejercicio.fintechapi.usuario.exceptions.UsuarioYaExisteException;
import org.ejercicio.fintechapi.usuario.mapper.UsuarioMapper;
import org.ejercicio.fintechapi.usuario.model.Usuario;
import org.ejercicio.fintechapi.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;


    @Override
    @Transactional
    public UsuarioResponseDTO save(UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new UsuarioYaExisteException("Ya existe un usuario con el email " + dto.email());
        }

        if (usuarioRepository.existsByNombreUsuario(dto.nombreUsuario())) {
            throw new UsuarioYaExisteException("Ya existe un usuario con el nombre de usuario " + dto.nombreUsuario());
        }

        return usuarioMapper.entityToResponseDTO(usuarioRepository.save(usuarioMapper.requestToEntity(dto)));
    }

    @Override
    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::entityToResponseDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO findByIdDto(Long id) {
        return usuarioMapper.entityToResponseDTO(this.findById(id));
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNoEncontradoException("no existe un usuario con el id: " + id));
    }
}
