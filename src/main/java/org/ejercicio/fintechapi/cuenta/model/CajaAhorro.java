package org.ejercicio.fintechapi.cuenta.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.ejercicio.fintechapi.usuario.model.Usuario;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("AHORRO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CajaAhorro extends Cuenta {

    public CajaAhorro(Usuario titular, String alias) {
        super(titular, alias);
    }

    @Override
    protected boolean puedeExtraer(BigDecimal monto) {

        return getSaldo().compareTo(monto) >= 0;
    }

    @Override
    public TipoDeCuenta getTipoDeCuenta() {
        return TipoDeCuenta.AHORRO;}
}
