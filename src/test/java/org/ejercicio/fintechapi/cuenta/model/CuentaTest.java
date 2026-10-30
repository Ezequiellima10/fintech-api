package org.ejercicio.fintechapi.cuenta.model;

import org.ejercicio.fintechapi.cuenta.exceptions.MontoInvalidoException;
import org.ejercicio.fintechapi.cuenta.exceptions.SaldoInsuficienteException;
import org.ejercicio.fintechapi.usuario.model.Usuario;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    private final Usuario titular = Usuario.builder().id(1L).nombreUsuario("juan").email("juan@mail.com").build();


    @Test
    void extraer_cajaAhorroTodoElSaldo_dejaSaldoEnCero() {
        // given
        Cuenta cuenta = new CajaAhorro(titular, "mi.caja");
        cuenta.depositar(new BigDecimal("100"));

        // when
        cuenta.extraer(new BigDecimal("100"));

        // then
        assertEquals(BigDecimal.ZERO, cuenta.getSaldo());
    }

    @Test
    void extraer_cajaAhorroMasQueElSaldo_lanzaSaldoInsuficienteException() {
        // given
        Cuenta cuenta = new CajaAhorro(titular, "mi.caja");
        cuenta.depositar(new BigDecimal("100"));

        // when / then
        assertThrows(SaldoInsuficienteException.class, () -> cuenta.extraer(new BigDecimal("101")));

        assertEquals(new BigDecimal("100"), cuenta.getSaldo());
    }

    @Test
    void extraer_cuentaCorrienteHastaElDescubierto_dejaSaldoNegativo() {
        // given
        Cuenta cuenta = new CuentaCorriente(titular, "mi.corriente", new BigDecimal("50"));
        cuenta.depositar(new BigDecimal("100"));

        // when
        cuenta.extraer(new BigDecimal("150"));

        // then
        assertEquals(new BigDecimal("-50"), cuenta.getSaldo());
    }

    @Test
    void extraer_cuentaCorrienteMasQueElDescubierto_lanzaSaldoInsuficienteException() {
        // given
        Cuenta cuenta = new CuentaCorriente(titular, "mi.corriente", new BigDecimal("50"));
        cuenta.depositar(new BigDecimal("100"));

        // when / then
        assertThrows(SaldoInsuficienteException.class, () -> cuenta.extraer(new BigDecimal("151")));

        assertEquals(new BigDecimal("100"), cuenta.getSaldo());
    }


    @Test
    void depositar_montoCero_lanzaMontoInvalidoException() {
        // given
        Cuenta cuenta = new CajaAhorro(titular, "mi.caja");

        // when / then
        assertThrows(MontoInvalidoException.class, () -> cuenta.depositar(BigDecimal.ZERO));

        assertEquals(BigDecimal.ZERO, cuenta.getSaldo());
    }
}
