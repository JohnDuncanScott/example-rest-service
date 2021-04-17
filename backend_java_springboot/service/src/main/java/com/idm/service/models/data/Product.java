package com.idm.service.models.data;

import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Product is read-only for the purpose of this exercise.
 */
@Value
public class Product {
    String id;
    String name;
    BigDecimal usdPrice;

    public Product(String id, String name, BigDecimal usdPrice) {
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
