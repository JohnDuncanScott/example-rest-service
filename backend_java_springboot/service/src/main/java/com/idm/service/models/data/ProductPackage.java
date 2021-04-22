package com.idm.service.models.data;

import lombok.NonNull;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

// TODO: Add full unit tests
@Value
public class ProductPackage {
    // TODO: A null id indicates an id needs to be generated, having something mutable on an immutable object
    // is a code smell, but for simplicity, going down this route
    @NonFinal
    @Setter
    private String id;
    @NonNull private String name;
    @NonNull private String description;
    @NonNull private Map<String, ProductWithQuantity> productIdToQuantityMap;

    // Use when creating a fresh object that will later have its id populated and associated data populated
    public ProductPackage(@NonNull String name, @NonNull String description) {
        checkNotBlank(name, "name");
        checkNotBlank(description, "description");

        this.name = name;
        this.description = description;
        this.productIdToQuantityMap = new HashMap<>();
    }

    // Constructor for easy setup
    public ProductPackage(
            @NonNull String id,
            @NonNull String name,
            @NonNull String description,
            @NonNull List<ProductWithQuantity> productIdToQuantityMap) {
        this(
                id,
                name,
                description,
                productIdToQuantityMap
                    .stream()
                    .collect(Collectors.toMap(ProductWithQuantity::getProductId, pq -> pq)));
    }

    // Constructor for cloning
    public ProductPackage(
            @NonNull String id,
            @NonNull String name,
            @NonNull String description,
            @NonNull Map<String, ProductWithQuantity> productIdToQuantityMap) {
        checkNotBlank(id, "id");
        checkNotBlank(name, "name");
        checkNotBlank(description, "description");

        this.id = id;
        this.name = name;
        this.description = description;
        this.productIdToQuantityMap = productIdToQuantityMap;
    }

    private void checkNotBlank(String value, String argumentName) {
        checkArgument(StringUtils.isNotBlank(value), argumentName + " cannot be blank");
    }
}
