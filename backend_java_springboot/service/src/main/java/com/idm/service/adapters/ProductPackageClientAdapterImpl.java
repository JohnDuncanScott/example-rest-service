package com.idm.service.adapters;

import com.google.common.collect.ImmutableList;
import com.idm.service.models.data.ProductPackage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

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
                new HashSet<>(Arrays.asList("shield")));
        ProductPackage weaponsPackage = new ProductPackage(
                Integer.toString(++idCounter),
                "Weapons package",
                "An assortment of weapons to get your new character started.",
                // Sword, Axe (Knife & Bow excluded)
                new HashSet<>(Arrays.asList("sword", "axe")));
        ProductPackage currencyPackage = new ProductPackage(
                Integer.toString(++idCounter),
                "Currency package",
                "An assortment of currency to get your new character started.",
                // Gold, Silver (Platinum excluded)
                new HashSet<>(Arrays.asList("gold", "silver")));
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
                    productPackage.getProductIds()
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
