package com.idm.service.models.resources;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductResource extends RepresentationModel<ProductResource> {
    // A null id indicates an id needs to be generated
    private String id;
    @NonNull private String name;
    @NonNull private Currency localCurrency;
    @NonNull private BigDecimal localPrice;
}
