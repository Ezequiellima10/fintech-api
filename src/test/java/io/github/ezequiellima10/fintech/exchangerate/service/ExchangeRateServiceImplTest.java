package io.github.ezequiellima10.fintech.exchangerate.service;

import io.github.ezequiellima10.fintech.exchangerate.dto.UsdExchangeRateDTO;
import io.github.ezequiellima10.fintech.exchangerate.exception.ExchangeRateUnavailableException;
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
 * Tests the DolarApi integration without going to the internet: MockRestServiceServer
 * stands in for the API and responds with whatever each test tells it to.
 */
class ExchangeRateServiceImplTest {

    private static final String OFFICIAL_USD_URL = "https://dolarapi.com/v1/dolares/oficial";

    private MockRestServiceServer mockServer;
    private ExchangeRateServiceImpl exchangeRateService;


    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://dolarapi.com/v1");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        exchangeRateService = new ExchangeRateServiceImpl(builder.build());
    }

    @Test
    void getOfficialUsdRate_apiRespondsOk_returnsExchangeRate() {
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
        mockServer.expect(requestTo(OFFICIAL_USD_URL))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        // when
        UsdExchangeRateDTO result = exchangeRateService.getOfficialUsdRate();

        // then
        assertEquals("USD", result.currency());
        assertEquals("oficial", result.type());
        assertEquals("Oficial", result.name());
        assertEquals(new BigDecimal("1510"), result.buy());
        assertEquals(new BigDecimal("1560"), result.sell());
        assertEquals(Instant.parse("2026-10-29T18:00:00Z"), result.updatedAt());
        mockServer.verify();
    }

    @Test
    void getOfficialUsdRate_apiReturnsError_throwsExchangeRateUnavailableException() {
        // given
        mockServer.expect(requestTo(OFFICIAL_USD_URL))
                .andRespond(withServerError());

        // when / then
        assertThrows(ExchangeRateUnavailableException.class, () -> exchangeRateService.getOfficialUsdRate());
    }

    @Test
    void getOfficialUsdRate_apiRespondsEmpty_throwsExchangeRateUnavailableException() {
        // given
        mockServer.expect(requestTo(OFFICIAL_USD_URL))
                .andRespond(withSuccess());

        // when / then
        assertThrows(ExchangeRateUnavailableException.class, () -> exchangeRateService.getOfficialUsdRate());
    }
}
