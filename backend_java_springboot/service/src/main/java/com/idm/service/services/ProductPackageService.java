package com.idm.service.services;

import com.idm.service.models.data.Product;
import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductPackageInstant;
import lombok.NonNull;

import java.util.Currency;
import java.util.List;

public interface ProductPackageService {
    List<ProductPackageInstant> getAll(@NonNull Currency currency);

    ProductPackageInstant getById(@NonNull String id, @NonNull Currency currency);

    List<Product> getProductsForPackage(@NonNull String packageId, @NonNull Currency currency);

    Product getProductForPackage(
            @NonNull String packageId,
            @NonNull String productId,
            @NonNull Currency currency);

    boolean deleteById(@NonNull String id);

    ProductPackageInstant save(
            @NonNull ProductPackage productPackage,
            @NonNull Currency currency);
}
