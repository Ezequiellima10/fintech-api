package org.ejercicio.fintechapi.usuario.repository;

import org.ejercicio.fintechapi.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    boolean existsByNombreUsuario(String nombreUsuario);

}

