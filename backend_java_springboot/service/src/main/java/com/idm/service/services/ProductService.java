package com.idm.service.services;

import com.google.common.collect.ImmutableMap;
import com.idm.service.models.data.Product;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class ProductService {
    private static Map<String, Product> productMap;

    static {
        productMap = new ImmutableMap.Builder<String, Product>()
                .put("VqKb4tyj9V6i", new Product("VqKb4tyj9V6i", "Shield", BigDecimal.valueOf(1149)))
                .put("DXSQpv6XVeJm", new Product("DXSQpv6XVeJm", "Helmet", BigDecimal.valueOf(999)))
                .put("7dgX6XzU3Wds", new Product("7dgX6XzU3Wds", "Sword", BigDecimal.valueOf(899)))
                .put("PKM5pGAh9yGm", new Product("PKM5pGAh9yGm", "Axe", BigDecimal.valueOf(799)))
                .put("7Hv0hA2nmci7", new Product("7Hv0hA2nmci7", "Knife", BigDecimal.valueOf(349)))
                .put("500R5EHvNlNB", new Product("500R5EHvNlNB", "Gold Coin", BigDecimal.valueOf(249)))
                .put("IP3cv7TcZhQn", new Product("IP3cv7TcZhQn", "Platinum Coin", BigDecimal.valueOf(399)))
                .put("IJOHGYkY2CYq", new Product("IJOHGYkY2CYq", "Bow", BigDecimal.valueOf(649)))
                .put("8anPsR2jbfNW", new Product("8anPsR2jbfNW", "Silver Coin", BigDecimal.valueOf(50)))
                .build();
    }

    public List<Product> getAll() {
        log.info("Found products, size: {}", productMap.size());
        return new ArrayList<>(productMap.values());
    }

    public Product getById(@NonNull String id) {
        Product product = productMap.get(id);

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
