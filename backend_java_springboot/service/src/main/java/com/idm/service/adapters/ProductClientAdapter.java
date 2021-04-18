package com.idm.service.adapters;

import com.idm.service.models.data.Product;
import lombok.NonNull;

import java.util.List;

public interface ProductClientAdapter {
    List<Product> getAllProducts();

    Product getById(@NonNull String id);
}
