package org.ejercicio.fintechapi.cotizacion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.cotizacion.dtos.CotizacionDolarDTO;
import org.ejercicio.fintechapi.cotizacion.service.CotizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cotizacion", description = "Obtiene la cotización del dólar actual")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @Operation(summary = "Busca la cotización actual del dólar")
    @ApiResponse(responseCode = "200", description = "Cotizacion obtenida")
    @ApiResponse(responseCode = "503", description = "La API de cotizacion no esta disponible")
    @GetMapping(path = "/cotizaciones/dolar")
    public ResponseEntity<CotizacionDolarDTO> getDolarOficial() {
        return ResponseEntity.ok(cotizacionService.getDolarOficial());
    }
}
