package com.idm.service.models.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Product is read-only for the purpose of this exercise.
 */
// TODO: Add full unit tests
@Value
public class Product {
    private static final int PRICE_SCALE = 2;
    String id;
    String name;
    BigDecimal usdPrice;

    @JsonCreator
    public Product(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("usdPrice") String usdPrice) {
        this(id, name, BigDecimal.valueOf(Long.valueOf(usdPrice), PRICE_SCALE));
    }

    public Product(
            String id,
            String name,
            BigDecimal usdPrice) {
        checkNotBlank(id, "id");
        checkNotBlank(name, "name");
        checkArgument(usdPrice.compareTo(BigDecimal.ZERO) >= 0, "price cannot be negative");

        this.id = id;
        this.name = name;
        this.usdPrice = usdPrice;
    }

    private void checkNotBlank(String value, String argumentName) {
        checkArgument(StringUtils.isNotBlank(value), argumentName + " cannot be blank");
    }
}
