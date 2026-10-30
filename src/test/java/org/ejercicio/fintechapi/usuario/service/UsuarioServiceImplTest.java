package org.ejercicio.fintechapi.usuario.service;

import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioRequestDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioResponseDTO;
import org.ejercicio.fintechapi.usuario.exceptions.UsuarioNoEncontradoException;
import org.ejercicio.fintechapi.usuario.exceptions.UsuarioYaExisteException;
import org.ejercicio.fintechapi.usuario.mapper.UsuarioMapper;
import org.ejercicio.fintechapi.usuario.model.Usuario;
import org.ejercicio.fintechapi.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;


    @Test
    void create_datosValidos_guardaYDevuelveDto() {
        // given
        UsuarioRequestDTO request = new UsuarioRequestDTO("juan", "juan@mail.com");
        Usuario usuarioNuevo = Usuario.builder().nombreUsuario("juan").email("juan@mail.com").build();
        Usuario usuarioGuardado = Usuario.builder().id(1L).nombreUsuario("juan").email("juan@mail.com").build();
        UsuarioResponseDTO respuestaEsperada = UsuarioResponseDTO.builder().id(1L).nombreUsuario("juan").email("juan@mail.com").build();

        when(usuarioRepository.existsByEmail("juan@mail.com")).thenReturn(false);
        when(usuarioRepository.existsByNombreUsuario("juan")).thenReturn(false);
        when(usuarioMapper.requestToEntity(request)).thenReturn(usuarioNuevo);
        when(usuarioRepository.save(usuarioNuevo)).thenReturn(usuarioGuardado);
        when(usuarioMapper.entityToResponseDTO(usuarioGuardado)).thenReturn(respuestaEsperada);

        // when
        UsuarioResponseDTO resultado = usuarioService.create(request);

        // then
        assertEquals(respuestaEsperada, resultado);
        verify(usuarioRepository).save(usuarioNuevo);
    }

    @Test
    void create_emailDuplicado_lanzaUsuarioYaExisteException() {
        // given
        UsuarioRequestDTO request = new UsuarioRequestDTO("juan", "juan@mail.com");

        when(usuarioRepository.existsByEmail("juan@mail.com")).thenReturn(true);

        // when / then
        assertThrows(UsuarioYaExisteException.class, () -> usuarioService.create(request));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void create_nombreUsuarioDuplicado_lanzaUsuarioYaExisteException() {
        // given
        UsuarioRequestDTO request = new UsuarioRequestDTO("juan", "juan@mail.com");

        when(usuarioRepository.existsByEmail("juan@mail.com")).thenReturn(false);
        when(usuarioRepository.existsByNombreUsuario("juan")).thenReturn(true);

        // when / then
        assertThrows(UsuarioYaExisteException.class, () -> usuarioService.create(request));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void findById_inexistente_lanzaUsuarioNoEncontradoException() {
        // given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.findById(99L));
    }

    @Test
    void findByIdDto_existente_devuelveDto() {
        // given
        Usuario usuario = Usuario.builder().id(1L).nombreUsuario("juan").email("juan@mail.com").build();
        UsuarioResponseDTO respuestaEsperada = UsuarioResponseDTO.builder().id(1L).nombreUsuario("juan").email("juan@mail.com").build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.entityToResponseDTO(usuario)).thenReturn(respuestaEsperada);

        // when
        UsuarioResponseDTO resultado = usuarioService.findByIdDto(1L);

        // then
        assertEquals(respuestaEsperada, resultado);
    }

    @Test
    void findAll_mapeaPaginaADtos() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Usuario juan = Usuario.builder().id(1L).nombreUsuario("juan").email("juan@mail.com").build();
        Usuario ana = Usuario.builder().id(2L).nombreUsuario("ana").email("ana@mail.com").build();
        Page<Usuario> pagina = new PageImpl<>(List.of(juan, ana), pageable, 2);

        UsuarioResponseDTO dtoJuan = UsuarioResponseDTO.builder().id(1L).nombreUsuario("juan").build();
        UsuarioResponseDTO dtoAna = UsuarioResponseDTO.builder().id(2L).nombreUsuario("ana").build();

        when(usuarioRepository.findAll(pageable)).thenReturn(pagina);
        when(usuarioMapper.entityToResponseDTO(juan)).thenReturn(dtoJuan);
        when(usuarioMapper.entityToResponseDTO(ana)).thenReturn(dtoAna);

        // when
        ResponsePageDTO<UsuarioResponseDTO> resultado = usuarioService.findAll(pageable);

        // then
        assertEquals(List.of(dtoJuan, dtoAna), resultado.dtos());
        assertEquals(2, resultado.totalElements());
        assertEquals(1, resultado.totalPages());
    }
}
