package org.ejercicio.fintechapi.usuario.controller;

import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.usuario.dtos.UsuarioResponseDTO;
import org.ejercicio.fintechapi.usuario.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @GetMapping(path = "/usuarios/{id}")
    public UsuarioResponseDTO getUsuario(@PathVariable Long id){

        return usuarioService.findByIdDto(id);
    }
}
