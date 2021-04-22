package com.idm.service.models.resources;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
// TODO: Should have a string representation of price so client can just display that without worrying about
// currency formatting
public class ProductPackageResource extends RepresentationModel<ProductPackageResource> {
    // A null id indicates an id needs to be generated
    private String id;
    @NonNull private String name;
    @NonNull private String description;
    @NonNull private Currency localCurrency;
    @NonNull private BigDecimal totalLocalPrice;
    @NonNull private List<ProductWithQuantityResource> productsWithQuantity;
}
