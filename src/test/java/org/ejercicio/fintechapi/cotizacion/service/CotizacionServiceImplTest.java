package org.ejercicio.fintechapi.cotizacion.service;

import org.ejercicio.fintechapi.cotizacion.dtos.CotizacionDolarDTO;
import org.ejercicio.fintechapi.cotizacion.exceptions.CotizacionNoDisponibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Prueba el consumo de DolarApi sin salir a internet: MockRestServiceServer
 * se hace pasar por la API y responde lo que cada test le indique.
 */
class CotizacionServiceImplTest {

    private static final String URL_DOLAR_OFICIAL = "https://dolarapi.com/v1/dolares/oficial";

    private MockRestServiceServer servidorSimulado;
    private CotizacionServiceImpl cotizacionService;


    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://dolarapi.com/v1");
        servidorSimulado = MockRestServiceServer.bindTo(builder).build();
        cotizacionService = new CotizacionServiceImpl(builder.build());
    }

    @Test
    void getDolarOficial_apiRespondeOk_devuelveCotizacion() {
        // given
        String json = """
                {
                  "moneda": "USD",
                  "casa": "oficial",
                  "nombre": "Oficial",
                  "compra": 1510,
                  "venta": 1560,
                  "fechaActualizacion": "2026-10-29T18:00:00.000Z"
                }
                """;
        servidorSimulado.expect(requestTo(URL_DOLAR_OFICIAL))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        // when
        CotizacionDolarDTO resultado = cotizacionService.getDolarOficial();

        // then
        assertEquals(new BigDecimal("1510"), resultado.compra());
        assertEquals(new BigDecimal("1560"), resultado.venta());
        assertEquals(Instant.parse("2026-10-29T18:00:00Z"), resultado.fechaActualizacion());
        servidorSimulado.verify();
    }

    @Test
    void getDolarOficial_apiDevuelveError_lanzaCotizacionNoDisponibleException() {
        // given
        servidorSimulado.expect(requestTo(URL_DOLAR_OFICIAL))
                .andRespond(withServerError());

        // when / then
        assertThrows(CotizacionNoDisponibleException.class, () -> cotizacionService.getDolarOficial());
    }

    @Test
    void getDolarOficial_apiRespondeVacio_lanzaCotizacionNoDisponibleException() {
        // given
        servidorSimulado.expect(requestTo(URL_DOLAR_OFICIAL))
                .andRespond(withSuccess());

        // when / then
        assertThrows(CotizacionNoDisponibleException.class, () -> cotizacionService.getDolarOficial());
    }
}
