package com.idm.service.models.data;

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

// TODO: For simplicity, a package can only contain a unique set of products
@Value
public class ProductPackage {
    // TODO: A null id indicates an id needs to be generated, having something mutable on an immutable object
    // is a code smell, but for simplicity, going down this route
    @NonFinal
    @Setter
    private String id;
    @NonNull private String name;
    @NonNull private String description;
    @NonNull private Set<String> productIds;

    // Use when creating a fresh object that will later have its id populated and associated data populated
    public ProductPackage(@NonNull String name, @NonNull String description) {
        checkNotBlank(name, "name");
        checkNotBlank(description, "description");

        this.name = name;
        this.description = description;
        this.productIds = ImmutableSet.of();
    }

    // Standard constructor for most use cases
    public ProductPackage(
            @NonNull String id,
            @NonNull String name,
            @NonNull String description,
            @NonNull Set<String> productIds) {
        checkNotBlank(id, "id");
        checkNotBlank(name, "name");
        checkNotBlank(description, "description");

        this.id = id;
        this.name = name;
        this.description = description;
        this.productIds = ImmutableSet.copyOf(productIds);
    }

    private void checkNotBlank(String value, String argumentName) {
        checkArgument(StringUtils.isNotBlank(value), argumentName + " cannot be blank");
    }
}
