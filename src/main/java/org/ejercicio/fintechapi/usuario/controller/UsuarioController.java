package org.ejercicio.fintechapi.usuario.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.shared.dto.ResponsePageDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioRequestDTO;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioResponseDTO;
import org.ejercicio.fintechapi.usuario.service.UsuarioService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;



    @GetMapping(path = "/usuarios/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuario(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.findByIdDto(id));
    }


    @PostMapping (path = "/usuarios")
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(request));
    }


    @GetMapping(path = "/usuarios")
    public ResponseEntity<ResponsePageDTO<UsuarioResponseDTO>> findAll(Pageable page) {
        return ResponseEntity.ok(usuarioService.findAll(page));
    }
}
