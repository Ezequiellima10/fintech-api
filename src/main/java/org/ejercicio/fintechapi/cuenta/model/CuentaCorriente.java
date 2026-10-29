package org.ejercicio.fintechapi.cuenta.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ejercicio.fintechapi.usuario.model.Usuario;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CORRIENTE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CuentaCorriente extends Cuenta {


    @Column(precision = 19, scale = 2)
    private BigDecimal limiteDescubierto;

    public CuentaCorriente(Usuario titular, String alias, BigDecimal limiteDescubierto) {
        super(titular, alias);
        this.limiteDescubierto = limiteDescubierto != null ? limiteDescubierto : BigDecimal.ZERO;
    }

    @Override
    protected boolean puedeExtraer(BigDecimal monto) {

        return getSaldo().add(limiteDescubierto).compareTo(monto) >= 0;
    }

    @Override
    public TipoDeCuenta getTipoDeCuenta() {
        return TipoDeCuenta.CORRIENTE;}
}
