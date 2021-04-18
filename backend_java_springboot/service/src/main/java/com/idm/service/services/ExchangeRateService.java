package com.idm.service.services;

import java.math.BigDecimal;
import java.util.Currency;

public interface ExchangeRateService {
    BigDecimal getExchangeRateForUsdTo(Currency currency);
}
