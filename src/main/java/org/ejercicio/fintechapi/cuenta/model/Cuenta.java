package org.ejercicio.fintechapi.cuenta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.ejercicio.fintechapi.usuario.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table (name = "cuentas")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario titular;

    @NotBlank
    private String alias;

    @Column(nullable = false,  updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
