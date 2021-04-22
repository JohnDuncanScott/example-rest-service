package com.idm.service.services;

import com.idm.service.adapters.ExchangeRateClientAdapter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private static final Currency USD = Currency.getInstance("USD");
    // TODO: Need TTL cache
    private final Map<Currency, BigDecimal> currencyCache = new HashMap<>();
    private final ExchangeRateClientAdapter exchangeRateClientAdapter;

    @Override
    public BigDecimal getExchangeRateForUsdTo(@NonNull Currency currency) {
        if (currencyCache.containsKey(currency)) {
            log.info("Currency found in cache: " + currency);
            return currencyCache.get(currency);
        }

        log.info("Currency not found in cache, calling service for currency: " + currency);
        BigDecimal exchangeRate;

        try {
            exchangeRate = exchangeRateClientAdapter.getExchangeRate(USD, currency);
            currencyCache.put(currency, exchangeRate);
        } catch (IOException ex) {
            log.info("Could not get exchange rate, using backup strategy");

            switch (currency.toString()) {
                case "USD":
                    exchangeRate = BigDecimal.ONE;
                    break;
                case "GBP":
                    exchangeRate = BigDecimal.valueOf(0.72);
                    break;
                case "EUR":
                    exchangeRate = BigDecimal.valueOf(0.83);
                    break;
                default:
                    throw new IllegalArgumentException("Currency requested has not been configured: " + currency);
            }
        }

        return exchangeRate;
    }
}
