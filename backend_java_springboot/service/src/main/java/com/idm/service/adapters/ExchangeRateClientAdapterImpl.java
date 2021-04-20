package com.idm.service.adapters;

import com.jayway.jsonpath.JsonPath;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Currency;

// TODO: Rather than query individual, grab a whole group and stick them in the cache, so only need to do 1 request
@Component
@Slf4j
public class ExchangeRateClientAdapterImpl implements ExchangeRateClientAdapter {
    private static final MathContext MATH_CONTEXT = new MathContext(6, RoundingMode.HALF_EVEN);

    @Inject
    private CloseableHttpClient httpClient;

    @Override
    public BigDecimal getExchangeRate(@NonNull Currency baseCurrency, @NonNull Currency targetCurrency)
            throws IOException {
        // TODO: Find a better API, this one does not support specifying a base currency
        // TODO: Move credentials to secure store
        String url =
                "http://api.exchangeratesapi.io/v1/latest?access_key=b7891d705fe676321bd1bd6bd3289366&symbols=USD,GBP,EUR";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            log.info("Exchange rate service response status: {}", response.getStatusLine());

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                log.info("Exchange rate service response content:\n{}", json);
                log.info("Exchange rate service call was successful, parsing content");

                BigDecimal baseRate = getExchangeRate(json, baseCurrency);
                BigDecimal targetRate = getExchangeRate(json, targetCurrency);

                return targetRate.divide(baseRate, MATH_CONTEXT);
            }
        // TODO: Shouldn't catch root exception, but just making sure this dependency is fully isolated
        } catch (Exception ex) {
            log.error("Failed while calling Products service", ex);
        }

        throw new IOException("Unable to retrieve exchange rate");
    }

    private BigDecimal getExchangeRate(String json, Currency currency) {
        String base = JsonPath.read(json, "$.base");
        final String jsonPathForRate = "$.rates." + currency;

        if (base.equals(currency.toString())) {
            // API returns base as an integer and JsonPath won't auto-convert a
            return BigDecimal.valueOf(JsonPath.<Integer>read(json, jsonPathForRate));
        }

        return BigDecimal.valueOf(JsonPath.<Double>read(json, jsonPathForRate));
    }
}
