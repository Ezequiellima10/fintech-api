package org.ejercicio.fintechapi.cotizacion.service;

import lombok.RequiredArgsConstructor;
import org.ejercicio.fintechapi.cotizacion.dtos.CotizacionDolarDTO;
import org.ejercicio.fintechapi.cotizacion.exceptions.CotizacionNoDisponibleException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class CotizacionServiceImpl implements CotizacionService {

    private final RestClient dolarApiRestClient;


    @Override
    public CotizacionDolarDTO getDolarOficial() {

        CotizacionDolarDTO cotizacion;

        try {
            cotizacion = dolarApiRestClient.get()
                    .uri("/dolares/oficial")
                    .retrieve()
                    .body(CotizacionDolarDTO.class);
        } catch (RestClientException e) {
            throw new CotizacionNoDisponibleException("No se pudo obtener la cotizacion del dolar oficial", e);
        }

        if (cotizacion == null) {
            throw new CotizacionNoDisponibleException("La API de cotizaciones devolvio una respuesta vacia");
        }

        return cotizacion;
    }
}
