package org.ejercicio.fintechapi.cuenta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ejercicio.fintechapi.cuenta.exceptions.MontoInvalidoException;
import org.ejercicio.fintechapi.cuenta.exceptions.SaldoInsuficienteException;
import org.ejercicio.fintechapi.usuario.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "titular_id", nullable = false)
    private Usuario titular;

    @Setter
    @NotBlank
    @Column(nullable = false, unique = true)
    private String alias;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;


    protected Cuenta(Usuario titular, String alias) {
        this.titular = titular;
        this.alias = alias;
    }

    @PrePersist
    void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public void depositar(BigDecimal monto) {
        validarMonto(monto);
        saldo = saldo.add(monto);
    }

    public void extraer(BigDecimal monto) {
        validarMonto(monto);
        if (!puedeExtraer(monto)) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
        saldo = saldo.subtract(monto);
    }

    protected abstract boolean puedeExtraer(BigDecimal monto);

    public abstract TipoDeCuenta getTipoDeCuenta();

    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new MontoInvalidoException("El monto debe ser mayor a cero");
        }
    }
}
