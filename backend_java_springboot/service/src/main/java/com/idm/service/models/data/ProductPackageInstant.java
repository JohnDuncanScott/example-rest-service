package com.idm.service.models.data;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Value
public class ProductPackageInstant {
    ProductPackage productPackage;
    BigDecimal totalUsdPrice;
    Currency localCurrency;
    BigDecimal localExchangeRate;
    Instant exchangeRateRetrievalTime;

    public BigDecimal getTotalLocalPrice() {
        return totalUsdPrice.multiply(localExchangeRate);
    }
}
