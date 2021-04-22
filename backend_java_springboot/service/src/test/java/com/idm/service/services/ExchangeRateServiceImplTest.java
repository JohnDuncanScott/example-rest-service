package com.idm.service.services;

import com.idm.service.adapters.ExchangeRateClientAdapter;
import com.idm.service.assertions.AssertMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceImplTest {
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateClientAdapter exchangeRateClientAdapter;

    @BeforeEach
    void setUp() throws IOException {
        lenient()
                .when(exchangeRateClientAdapter.getExchangeRate(any(), any()))
                .thenThrow(new IOException("Mocked IOException"));
        exchangeRateService =  new ExchangeRateServiceImpl(exchangeRateClientAdapter);
    }

    @Test
    void getExchangeRateForUsdToThrowsOnNullCurrency() {
        AssertMessages.assertThatLombokNonNullIsPresent(
                () -> exchangeRateService.getExchangeRateForUsdTo(null), "currency");
    }

    @ParameterizedTest
    @CsvSource({
            "USD,1",
            "GBP,0.72",
            "EUR,0.83"
    })
    void getExchangeRateForUsdToReturnsOneForUsd(String currencySymbol, BigDecimal expectedValue) {
        assertThat(
                exchangeRateService.getExchangeRateForUsdTo(Currency.getInstance(currencySymbol)),
                equalTo(expectedValue));
    }
}