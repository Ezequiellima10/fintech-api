package org.ejercicio.fintechapi.cuenta.mapper;

import org.ejercicio.fintechapi.cuenta.dtos.CuentaResponseDTO;
import org.ejercicio.fintechapi.cuenta.model.Cuenta;
import org.ejercicio.fintechapi.cuenta.model.CuentaCorriente;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CuentaMapper {

    public CuentaResponseDTO entityToResponse(Cuenta cuenta) {
        BigDecimal limiteDescubierto = cuenta instanceof CuentaCorriente ? ((CuentaCorriente) cuenta).getLimiteDescubierto() : BigDecimal.ZERO;
        return CuentaResponseDTO.builder().id(cuenta.getId())
                                            .alias(cuenta.getAlias())
                                            .tipoDeCuenta(cuenta.getTipoDeCuenta())
                                            .saldo(cuenta.getSaldo())
                                            .limiteDescubierto(limiteDescubierto)
                                            .titularId(cuenta.getTitular().getId())
                                            .build();
    }
}
