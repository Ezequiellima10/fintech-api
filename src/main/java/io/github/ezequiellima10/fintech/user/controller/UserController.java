package io.github.ezequiellima10.fintech.user.controller;

import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.github.ezequiellima10.fintech.user.dto.UserRequestDTO;
import io.github.ezequiellima10.fintech.user.dto.UserResponseDTO;
import io.github.ezequiellima10.fintech.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Alta, consulta de usuarios")
public class UserController {

    private final UserService userService;



    @Operation(summary = "Busca un usuario por id")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id")
    @GetMapping(path = "/api/users/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.findByIdDto(id));
    }


    @Operation(summary = "Crea un usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese email o nombre de usuario")
    @PostMapping (path = "/api/users")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }


    @Operation(summary = "Busca todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Usuarios encontrados (puede venir vacía)")
    @GetMapping(path = "/api/users")
    public ResponseEntity<ResponsePageDTO<UserResponseDTO>> findAll(@ParameterObject Pageable page) {
        return ResponseEntity.ok(userService.findAll(page));
    }
}
