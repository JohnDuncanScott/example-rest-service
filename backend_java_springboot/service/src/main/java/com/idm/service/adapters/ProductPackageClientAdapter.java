package com.idm.service.adapters;

import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductPackageInstant;
import lombok.NonNull;

import java.util.List;

public interface ProductPackageClientAdapter {
    List<ProductPackage> getAllProductPackages();
    ProductPackage getById(@NonNull String id);
    ProductPackage deleteById(@NonNull String id);
    ProductPackage save(@NonNull ProductPackage productPackage);
}
