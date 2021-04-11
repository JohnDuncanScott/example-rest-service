package com.idm.service.services;

import com.idm.service.models.data.Product;
import com.idm.service.models.data.ProductPackage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductPackageService {
    private static Map<String, ProductPackage> productPackagesMap = new HashMap<>();
    private static int idCounter = 0;

    static {
        ProductPackage armorPackage = new ProductPackage(
                Integer.toString(++idCounter),
                "Armor package",
                "An assortment of armor to get your new character started.",
                // Shield (Helmet excluded)
                new HashSet<>(Arrays.asList("VqKb4tyj9V6i")));
        ProductPackage weaponsPackage = new ProductPackage(
                Integer.toString(++idCounter),
                "Weapons package",
                "An assortment of weapons to get your new character started.",
                // Sword, Axe (Knife & Bow excluded)
                new HashSet<>(Arrays.asList("7dgX6XzU3Wds", "PKM5pGAh9yGm")));
        ProductPackage currencyPackage = new ProductPackage(
                Integer.toString(++idCounter),
                "Currency package",
                "An assortment of currency to get your new character started.",
                // Gold, Silver (Platinum excluded)
                new HashSet<>(Arrays.asList("500R5EHvNlNB", "8anPsR2jbfNW")));
        productPackagesMap.put(armorPackage.getId(), armorPackage);
        productPackagesMap.put(weaponsPackage.getId(), weaponsPackage);
        productPackagesMap.put(currencyPackage.getId(), currencyPackage);
    }

    @Autowired
    private ProductService productService;

    public List<ProductPackage> getAll() {
        log.info("Found product packages, size: {}", productPackagesMap.size());
        return new ArrayList<>(productPackagesMap.values());
    }

    public ProductPackage getById(@NonNull String id) {
        ProductPackage productPackage = productPackagesMap.get(id);

        if (productPackage != null) {
            log.info("Product package found: {}", productPackage);
            return productPackage;
        }

        logNothingFound(id);

        return null;
    }

    public List<Product> getProductsForPackage(@NonNull String packageId) {
        ProductPackage productPackage = getById(packageId);

        if (productPackage != null) {
            return productPackage.getProductIds().stream()
                    .map(productId -> productService.getById(productId))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }

    public Product getProductForPackage(@NonNull String packageId, @NonNull String productId) {
        ProductPackage productPackage = getById(packageId);

        if (productPackage != null) {
            if (!productPackage.getProductIds().contains(productId)) {
                log.info("Could not find product id '{}' in package with id '{}'", productId, packageId);
                return null;
            }

            Product product = productService.getById(productId);

            if (product == null) {
                log.error("Package with id '{}' contains invalid product id: {}", packageId, productId);
                return null;
            }

            return product;
        }

        return null;
    }

    public ProductPackage deleteById(@NonNull String id) {
        ProductPackage productPackage = productPackagesMap.remove(id);

        if (productPackage != null) {
            log.info("Removed product package, id: {}", id);
            return productPackage;
        }

        log.info("Could not remove product package, id not found: {}", id);

        return null;
    }

    public ProductPackage save(@NonNull ProductPackage productPackage) {
        if (productPackage.getId() == null) {
            productPackage.setId(Integer.toString(++idCounter));
            productPackagesMap.put(productPackage.getId(), productPackage);
            log.info("Added new product package: {}", productPackage);
        } else {
            deleteById(productPackage.getId());
            productPackagesMap.put(productPackage.getId(), productPackage);
            log.info("Updated product package: {}", productPackage);
        }

        return productPackage;
    }

    private void logNothingFound(String id) {
        log.info("No product package found for id: {}", id);
    }
}
