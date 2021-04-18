package com.idm.service.adapters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.idm.service.models.data.Product;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

// TODO: This should be replaced by the real backend service
@Component
public class ProductClientAdapterImpl implements ProductClientAdapter {
    private static final int SCALE = 2;
    private static Map<String, Product> productMap;

    static {
        productMap = new ImmutableMap.Builder<String, Product>()
                .put("VqKb4tyj9V6i", new Product("VqKb4tyj9V6i", "Shield", BigDecimal.valueOf(1149, SCALE)))
                .put("DXSQpv6XVeJm", new Product("DXSQpv6XVeJm", "Helmet", BigDecimal.valueOf(999, SCALE)))
                .put("7dgX6XzU3Wds", new Product("7dgX6XzU3Wds", "Sword", BigDecimal.valueOf(899, SCALE)))
                .put("PKM5pGAh9yGm", new Product("PKM5pGAh9yGm", "Axe", BigDecimal.valueOf(799, SCALE)))
                .put("7Hv0hA2nmci7", new Product("7Hv0hA2nmci7", "Knife", BigDecimal.valueOf(349, SCALE)))
                .put("500R5EHvNlNB", new Product("500R5EHvNlNB", "Gold Coin", BigDecimal.valueOf(249, SCALE)))
                .put("IP3cv7TcZhQn", new Product("IP3cv7TcZhQn", "Platinum Coin", BigDecimal.valueOf(399, SCALE)))
                .put("IJOHGYkY2CYq", new Product("IJOHGYkY2CYq", "Bow", BigDecimal.valueOf(649, SCALE)))
                .put("8anPsR2jbfNW", new Product("8anPsR2jbfNW", "Silver Coin", BigDecimal.valueOf(50, SCALE)))
                .build();
    }

    @Override
    public List<Product> getAllProducts() {
        return ImmutableList.copyOf(productMap.values());
    }

    @Override
    public Product getById(@NonNull String id) {
        return productMap.get(id);
    }
}
