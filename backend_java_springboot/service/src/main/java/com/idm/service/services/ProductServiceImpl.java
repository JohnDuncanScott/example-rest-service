package com.idm.service.services;

import com.idm.service.adapters.ProductClientAdapter;
import com.idm.service.models.data.Product;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ProductServiceImpl implements ProductService {
    private final ProductClientAdapter productClientAdapter;

    @Override
    public List<Product> getAll() {
        List<Product> products = productClientAdapter.getAllProducts();
        log.info("Found products, size: {}", products.size());
        return products;
    }

    @Override
    public Product getById(@NonNull String id) {
        Product product = productClientAdapter.getById(id);

        if (product != null) {
            log.info("Product found: {}", product);
            return product;
        }

        logNothingFound(id);

        return null;
    }

    private void logNothingFound(String id) {
        log.info("No product found for id: {}", id);
    }
}
