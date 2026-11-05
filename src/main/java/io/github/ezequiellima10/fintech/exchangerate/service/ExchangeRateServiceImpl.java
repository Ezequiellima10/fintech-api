package io.github.ezequiellima10.fintech.exchangerate.service;

import io.github.ezequiellima10.fintech.exchangerate.dto.UsdExchangeRateDTO;
import io.github.ezequiellima10.fintech.exchangerate.exception.ExchangeRateUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final RestClient dolarApiRestClient;


    @Override
    public UsdExchangeRateDTO getOfficialUsdRate() {

        UsdExchangeRateDTO exchangeRate;

        try {
            exchangeRate = dolarApiRestClient.get()
                    .uri("/dolares/oficial")
                    .retrieve()
                    .body(UsdExchangeRateDTO.class);
        } catch (RestClientException e) {
            throw new ExchangeRateUnavailableException("No se pudo obtener la cotizacion del dolar oficial", e);
        }

        if (exchangeRate == null) {
            throw new ExchangeRateUnavailableException("La API de cotizaciones devolvio una respuesta vacia");
        }

        return exchangeRate;
    }
}
