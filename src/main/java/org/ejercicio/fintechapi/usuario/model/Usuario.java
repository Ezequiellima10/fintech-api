package org.ejercicio.fintechapi.usuario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false,  updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
