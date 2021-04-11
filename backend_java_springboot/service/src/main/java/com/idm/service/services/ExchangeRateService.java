package com.idm.service.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
public class ExchangeRateService {
    public BigDecimal getExchangeRateForUsdTo(Currency currency) {
        // TODO: Use proper service
        String currencyCode = currency.getCurrencyCode();

        switch (currencyCode) {
            case "USD":
                return BigDecimal.ONE;
            case "GBP":
                return BigDecimal.valueOf(0.8);
            case "EUR":
                return BigDecimal.valueOf(0.5);
            default:
                throw new IllegalArgumentException("Currency code not found: " + currencyCode);
        }
    }
}
