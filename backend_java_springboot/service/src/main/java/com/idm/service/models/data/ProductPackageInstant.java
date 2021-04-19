package com.idm.service.models.data;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Currency;
import java.util.List;

// TODO: Add full unit tests
@Value
public class ProductPackageInstant {
    ProductPackage productPackage;
    List<Product> products;
    BigDecimal totalUsdPrice;
    Currency localCurrency;
    BigDecimal localExchangeRate;
    Instant exchangeRateRetrievalTime;

    // TODO: Too much coupling here between Product and Package, did this for speed
    public BigDecimal getProductLocalPrice(@NonNull Product product) {
        return setScale(product.getUsdPrice().multiply(localExchangeRate));
    }

    public BigDecimal getTotalLocalPrice() {
        return setScale(totalUsdPrice.multiply(localExchangeRate));
    }

    private static BigDecimal setScale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_EVEN);
    }
}
