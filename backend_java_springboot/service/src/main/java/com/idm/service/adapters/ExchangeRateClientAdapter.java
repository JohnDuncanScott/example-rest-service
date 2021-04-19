package com.idm.service.adapters;

import lombok.NonNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

public interface ExchangeRateClientAdapter {
    BigDecimal getExchangeRate(@NonNull Currency baseCurrency, @NonNull Currency targetCurrency) throws IOException;
}
