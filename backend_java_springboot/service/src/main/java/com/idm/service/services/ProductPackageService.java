package com.idm.service.services;

import com.idm.service.models.data.Product;
import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductPackageInstant;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
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

    @Autowired
    private ExchangeRateService exchangeRateService;

    public List<ProductPackageInstant> getAll(@NonNull Currency currency) {
        log.info("Found product packages, size: {}", productPackagesMap.size());

        return productPackagesMap.values().stream()
                .map(p -> toProductPackageInstant(p, currency))
                .collect(Collectors.toList());
    }

    public ProductPackageInstant getById(@NonNull String id, @NonNull Currency currency) {
        ProductPackage productPackage = productPackagesMap.get(id);

        if (productPackage != null) {
            log.info("Product package found: {}", productPackage);
            return toProductPackageInstant(productPackage, currency);
        }

        logNothingFound(id);

        return null;
    }

    public List<Product> getProductsForPackage(@NonNull String packageId, @NonNull Currency currency) {
        ProductPackageInstant productPackage = getById(packageId, currency);

        if (productPackage != null) {
            return productPackage.getProductPackage().getProductIds().stream()
                    .map(productId -> productService.getById(productId))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }

    public Product getProductForPackage(
            @NonNull String packageId,
            @NonNull String productId,
            @NonNull Currency currency) {
        ProductPackageInstant productPackageInstant = getById(packageId, currency);

        if (productPackageInstant != null) {
            if (!productPackageInstant.getProductPackage().getProductIds().contains(productId)) {
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

    public boolean deleteById(@NonNull String id) {
        ProductPackage productPackage = productPackagesMap.remove(id);

        if (productPackage != null) {
            log.info("Removed product package, id: {}", id);
            return true;
        }

        log.info("Could not remove product package, id not found: {}", id);

        return false;
    }

    public ProductPackageInstant save(
            @NonNull ProductPackage productPackage,
            @NonNull Currency currency) {
        if (productPackage.getId() == null) {
            // TODO: Not thread safe
            productPackage.setId(Integer.toString(++idCounter));
            productPackagesMap.put(productPackage.getId(), productPackage);
            log.info("Added new product package: {}", productPackage);
        } else {
            deleteById(productPackage.getId());
            productPackagesMap.put(productPackage.getId(), productPackage);
            log.info("Updated product package: {}", productPackage);
        }

        return toProductPackageInstant(productPackage, currency);
    }

    // TODO: Pricing concerns and conversions should probably be in a separate class since you don't necessarily
    // need to do an exchange unless you're displaying to the customer
    private ProductPackageInstant toProductPackageInstant(ProductPackage productPackage, Currency localCurrency) {
        List<Product> products = productPackage.getProductIds().stream()
                .map(productId -> productService.getById(productId))
                .collect(Collectors.toList());
        BigDecimal totalUsdPrice = products.stream()
                .map(Product::getUsdPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ProductPackageInstant(
                productPackage,
                products,
                totalUsdPrice,
                localCurrency,
                exchangeRateService.getExchangeRateForUsdTo(localCurrency),
                Instant.now()
        );
    }

    private void logNothingFound(String id) {
        log.info("No product package found for id: {}", id);
    }
}
