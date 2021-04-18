package com.idm.service.services;

import com.idm.service.assertions.AssertMessages;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

class ExchangeRateServiceImplTest {
    private ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl();

    @Test
    void getExchangeRateForUsdToThrowsOnNullCurrency() {
        AssertMessages.assertThatLombokNonNullIsPresent(
                () -> exchangeRateService.getExchangeRateForUsdTo(null), "currency");
    }

    @Test
    void getExchangeRateForUsdToReturnsOneForUsd() {
        BigDecimal actualValue = exchangeRateService.getExchangeRateForUsdTo(Currency.getInstance("USD"));

        assertThat(actualValue, equalTo(BigDecimal.ONE));
    }

    @Test
    void getExchangeRateForUsdToReturnsNonOneForNonUsd() {
        BigDecimal actualValue = exchangeRateService.getExchangeRateForUsdTo(Currency.getInstance("EUR"));

        assertThat(actualValue, not(equalTo(BigDecimal.ONE)));
    }
}