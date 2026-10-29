package org.ejercicio.fintechapi.cuenta.repository;

import org.ejercicio.fintechapi.cuenta.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta,Long> {

    boolean existsByAlias(String alias);

}
