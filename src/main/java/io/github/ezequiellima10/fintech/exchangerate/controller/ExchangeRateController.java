package io.github.ezequiellima10.fintech.exchangerate.controller;

import io.github.ezequiellima10.fintech.exchangerate.dto.UsdExchangeRateDTO;
import io.github.ezequiellima10.fintech.exchangerate.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cotizacion", description = "Obtiene la cotización del dólar actual")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Operation(summary = "Busca la cotización actual del dólar")
    @ApiResponse(responseCode = "200", description = "Cotizacion obtenida")
    @ApiResponse(responseCode = "503", description = "La API de cotizacion no esta disponible")
    @GetMapping(path = "/api/exchange-rates/usd")
    public ResponseEntity<UsdExchangeRateDTO> getOfficialUsdRate() {
        return ResponseEntity.ok(exchangeRateService.getOfficialUsdRate());
    }
}
