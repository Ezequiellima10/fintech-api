package org.ejercicio.fintechapi.cotizacion.controller;

import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.cotizacion.dtos.CotizacionDolarDTO;
import org.ejercicio.fintechapi.cotizacion.service.CotizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionService cotizacionService;


    @GetMapping(path = "/cotizaciones/dolar")
    public ResponseEntity<CotizacionDolarDTO> getDolarOficial() {
        return ResponseEntity.ok(cotizacionService.getDolarOficial());
    }
}
