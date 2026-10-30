package org.ejercicio.fintechapi.cuenta.service;

import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaRequestUpdateDTO;
import org.ejercicio.fintechapi.cuenta.dtos.CuentaResponseDTO;
import org.ejercicio.fintechapi.cuenta.exceptions.CuentaNoEncontradaException;
import org.ejercicio.fintechapi.cuenta.exceptions.CuentaYaExisteException;
import org.ejercicio.fintechapi.cuenta.mapper.CuentaMapper;
import org.ejercicio.fintechapi.cuenta.model.CajaAhorro;
import org.ejercicio.fintechapi.cuenta.model.Cuenta;
import org.ejercicio.fintechapi.cuenta.model.CuentaCorriente;
import org.ejercicio.fintechapi.cuenta.model.TipoDeCuenta;
import org.ejercicio.fintechapi.cuenta.repository.CuentaRepository;
import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.ejercicio.fintechapi.usuario.model.Usuario;
import org.ejercicio.fintechapi.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private CuentaMapper cuentaMapper;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private final Usuario titular = Usuario.builder().id(1L).nombreUsuario("juan").email("juan@mail.com").build();


    @Test
    void create_cajaAhorro_guardaCajaAhorroConTitular() {
        // given
        CuentaRequestDTO request = new CuentaRequestDTO(1L, "mi.caja", TipoDeCuenta.AHORRO, null);
        CajaAhorro cuentaGuardada = new CajaAhorro(titular, "mi.caja");
        CuentaResponseDTO respuestaEsperada = CuentaResponseDTO.builder().alias("mi.caja").tipoDeCuenta(TipoDeCuenta.AHORRO).build();

        when(cuentaRepository.existsByAlias("mi.caja")).thenReturn(false);
        when(usuarioService.findById(1L)).thenReturn(titular);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaGuardada);
        when(cuentaMapper.entityToResponse(cuentaGuardada)).thenReturn(respuestaEsperada);

        // when
        CuentaResponseDTO resultado = cuentaService.create(request);

        // then
        assertEquals(respuestaEsperada, resultado);

        // el captor "atrapa" la cuenta que el service le pasó a save() para poder revisarla
        ArgumentCaptor<Cuenta> captor = ArgumentCaptor.forClass(Cuenta.class);
        verify(cuentaRepository).save(captor.capture());
        Cuenta cuentaAGuardar = captor.getValue();

        assertInstanceOf(CajaAhorro.class, cuentaAGuardar);
        assertEquals("mi.caja", cuentaAGuardar.getAlias());
        assertEquals(titular, cuentaAGuardar.getTitular());
    }

    @Test
    void create_cuentaCorriente_guardaConLimiteDescubierto() {
        // given
        CuentaRequestDTO request = new CuentaRequestDTO(1L, "mi.corriente", TipoDeCuenta.CORRIENTE, new BigDecimal("5000"));
        CuentaCorriente cuentaGuardada = new CuentaCorriente(titular, "mi.corriente", new BigDecimal("5000"));
        CuentaResponseDTO respuestaEsperada = CuentaResponseDTO.builder().alias("mi.corriente").tipoDeCuenta(TipoDeCuenta.CORRIENTE).build();

        when(cuentaRepository.existsByAlias("mi.corriente")).thenReturn(false);
        when(usuarioService.findById(1L)).thenReturn(titular);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaGuardada);
        when(cuentaMapper.entityToResponse(cuentaGuardada)).thenReturn(respuestaEsperada);

        // when
        CuentaResponseDTO resultado = cuentaService.create(request);

        // then
        assertEquals(respuestaEsperada, resultado);

        ArgumentCaptor<Cuenta> captor = ArgumentCaptor.forClass(Cuenta.class);
        verify(cuentaRepository).save(captor.capture());
        Cuenta cuentaAGuardar = captor.getValue();

        assertInstanceOf(CuentaCorriente.class, cuentaAGuardar);
        assertEquals(new BigDecimal("5000"), ((CuentaCorriente) cuentaAGuardar).getLimiteDescubierto());
    }

    @Test
    void create_aliasDuplicado_lanzaCuentaYaExisteException() {
        // given
        CuentaRequestDTO request = new CuentaRequestDTO(1L, "mi.caja", TipoDeCuenta.AHORRO, null);

        when(cuentaRepository.existsByAlias("mi.caja")).thenReturn(true);

        // when / then
        assertThrows(CuentaYaExisteException.class, () -> cuentaService.create(request));

        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void update_aliasNuevo_actualizaAlias() {
        // given
        Cuenta cuenta = new CajaAhorro(titular, "alias.viejo");
        CuentaRequestUpdateDTO request = new CuentaRequestUpdateDTO("alias.nuevo");
        CuentaResponseDTO respuestaEsperada = CuentaResponseDTO.builder().alias("alias.nuevo").build();

        when(cuentaRepository.findById(10L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.existsByAlias("alias.nuevo")).thenReturn(false);
        when(cuentaMapper.entityToResponse(cuenta)).thenReturn(respuestaEsperada);

        // when
        CuentaResponseDTO resultado = cuentaService.update(10L, request);

        // then
        assertEquals("alias.nuevo", cuenta.getAlias());
        assertEquals(respuestaEsperada, resultado);
    }

    @Test
    void update_aliasDeOtraCuenta_lanzaCuentaYaExisteException() {
        // given
        Cuenta cuenta = new CajaAhorro(titular, "alias.viejo");
        CuentaRequestUpdateDTO request = new CuentaRequestUpdateDTO("alias.ocupado");

        when(cuentaRepository.findById(10L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.existsByAlias("alias.ocupado")).thenReturn(true);

        // when / then
        assertThrows(CuentaYaExisteException.class, () -> cuentaService.update(10L, request));

        assertEquals("alias.viejo", cuenta.getAlias());
    }

    @Test
    void update_cuentaInexistente_lanzaCuentaNoEncontradaException() {
        // given
        CuentaRequestUpdateDTO request = new CuentaRequestUpdateDTO("alias.nuevo");

        when(cuentaRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(CuentaNoEncontradaException.class, () -> cuentaService.update(99L, request));
    }

    @Test
    void findById_inexistente_lanzaCuentaNoEncontradaException() {
        // given
        when(cuentaRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(CuentaNoEncontradaException.class, () -> cuentaService.findById(99L));
    }

    @Test
    void findAll_mapeaPaginaADtos() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Cuenta caja = new CajaAhorro(titular, "caja.uno");
        Cuenta corriente = new CuentaCorriente(titular, "corriente.uno", new BigDecimal("1000"));
        Page<Cuenta> pagina = new PageImpl<>(List.of(caja, corriente), pageable, 2);

        CuentaResponseDTO dtoCaja = CuentaResponseDTO.builder().alias("caja.uno").build();
        CuentaResponseDTO dtoCorriente = CuentaResponseDTO.builder().alias("corriente.uno").build();

        when(cuentaRepository.findAll(pageable)).thenReturn(pagina);
        when(cuentaMapper.entityToResponse(caja)).thenReturn(dtoCaja);
        when(cuentaMapper.entityToResponse(corriente)).thenReturn(dtoCorriente);

        // when
        ResponsePageDTO<CuentaResponseDTO> resultado = cuentaService.findAll(pageable);

        // then
        assertEquals(List.of(dtoCaja, dtoCorriente), resultado.dtos());
        assertEquals(0, resultado.pageNumber());
        assertEquals(10, resultado.pageSize());
        assertEquals(2, resultado.totalElements());
        assertEquals(1, resultado.totalPages());
    }

    @Test
    void delete_existente_borraCuenta() {
        // given
        when(cuentaRepository.existsById(10L)).thenReturn(true);

        // when
        cuentaService.delete(10L);

        // then
        verify(cuentaRepository).deleteById(10L);
    }

    @Test
    void delete_inexistente_lanzaExcepcionYNoBorra() {
        // given
        when(cuentaRepository.existsById(99L)).thenReturn(false);

        // when / then
        assertThrows(CuentaNoEncontradaException.class, () -> cuentaService.delete(99L));

        verify(cuentaRepository, never()).deleteById(any());
    }
}
