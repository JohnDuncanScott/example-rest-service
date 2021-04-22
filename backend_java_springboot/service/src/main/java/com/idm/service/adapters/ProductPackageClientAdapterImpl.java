package com.idm.service.adapters;

import com.google.common.collect.ImmutableList;
import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductWithQuantity;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ProductPackageClientAdapterImpl implements ProductPackageClientAdapter {
    private static Map<String, ProductPackage> productPackagesMap = new HashMap<>();
    private static int idCounter = 0;

    static {
        ProductPackage armorPackage = new ProductPackage(
                Integer.toString(++idCounter),
                "Armor package",
                "An assortment of armor to get your new character started.",
                // Shield (Helmet excluded)
                Arrays.asList(new ProductWithQuantity("VqKb4tyj9V6i", 1)));
        ProductPackage weaponsPackage = new ProductPackage(
                Integer.toString(++idCounter),
                "Weapons package",
                "An assortment of weapons to get your new character started.",
                // Sword, Axe (Knife & Bow excluded)
                Arrays.asList(
                        new ProductWithQuantity("7dgX6XzU3Wds", 1),
                        new ProductWithQuantity("PKM5pGAh9yGm", 1)));
        ProductPackage currencyPackage = new ProductPackage(
                Integer.toString(++idCounter),
                "Currency package",
                "An assortment of currency to get your new character started.",
                // Gold, Silver (Platinum excluded)
                Arrays.asList(
                        new ProductWithQuantity("500R5EHvNlNB", 1),
                        new ProductWithQuantity("8anPsR2jbfNW", 1)));
        productPackagesMap.put(armorPackage.getId(), armorPackage);
        productPackagesMap.put(weaponsPackage.getId(), weaponsPackage);
        productPackagesMap.put(currencyPackage.getId(), currencyPackage);
    }

    @Override
    public List<ProductPackage> getAllProductPackages() {
        return ImmutableList.copyOf(productPackagesMap.values());
    }

    @Override
    public ProductPackage getById(@NonNull String id) {
        return productPackagesMap.get(id);
    }

    @Override
    public ProductPackage deleteById(@NonNull String id) {
        return productPackagesMap.remove(id);
    }

    @Override
    public ProductPackage save(@NonNull ProductPackage productPackage) {
        if (productPackage.getId() == null) {
            // TODO: Replacing with proper safe id generation
            ProductPackage newProductPackage = new ProductPackage(
                    Integer.toString(++idCounter),
                    productPackage.getName(),
                    productPackage.getDescription(),
                    productPackage.getProductIdToQuantityMap()
            );
            productPackagesMap.put(newProductPackage.getId(), newProductPackage);
            log.info("Added new product package: {}", newProductPackage);
            return newProductPackage;
        } else {
            deleteById(productPackage.getId());
            productPackagesMap.put(productPackage.getId(), productPackage);
            log.info("Updated product package: {}", productPackage);
            return productPackage;
        }
    }
}
