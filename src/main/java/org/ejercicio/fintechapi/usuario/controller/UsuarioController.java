package org.ejercicio.fintechapi.usuario.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioRequestDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioResponseDTO;
import org.ejercicio.fintechapi.usuario.service.UsuarioService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Alta, consulta, modificación de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;



    @Operation(summary = "Busca un usuario por id")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id")
    @GetMapping(path = "/usuarios/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuario(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.findByIdDto(id));
    }


    @Operation(summary = "Crea un usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese email o nombre de usuario")
    @PostMapping (path = "/usuarios")
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(request));
    }


    @Operation(summary = "Busca todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Usuarios encontrados (puede venir vacía)")
    @GetMapping(path = "/usuarios")
    public ResponseEntity<ResponsePageDTO<UsuarioResponseDTO>> findAll(@ParameterObject Pageable page) {
        return ResponseEntity.ok(usuarioService.findAll(page));
    }
}
