package com.idm.service.models.data;

import lombok.Value;

// TODO: Should do input validation (id not blank, quantity > 0)
@Value
public class ProductWithQuantity {
    String productId;
    int quantity;
}
