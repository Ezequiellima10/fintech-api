package io.github.ezequiellima10.fintech.exchangerate.service;

import io.github.ezequiellima10.fintech.exchangerate.dto.UsdExchangeRateDTO;

public interface ExchangeRateService {

    UsdExchangeRateDTO getOfficialUsdRate();
}
