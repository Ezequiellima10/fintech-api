package org.ejercicio.fintechapi.usuario.mapper;

import org.ejercicio.fintechapi.usuario.dtos.UsuarioRequestDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioResponseDTO;
import org.ejercicio.fintechapi.usuario.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario requestToEntity(UsuarioRequestDTO dto) {
       return Usuario.builder()
                .nombreUsuario(dto.nombreUsuario())
                .email(dto.email())
                .build();
    }

    public UsuarioResponseDTO entityToResponseDTO(Usuario entity) {
        return UsuarioResponseDTO.builder()
                .id(entity.getId())
                .nombreUsuario(entity.getNombreUsuario())
                .email(entity.getEmail())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}
