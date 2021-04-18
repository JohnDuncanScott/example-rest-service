package com.idm.service.services;

import com.idm.service.adapters.ProductPackageClientAdapter;
import com.idm.service.models.data.Product;
import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductPackageInstant;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ProductPackageServiceImpl implements ProductPackageService {
    private final ProductService productService;
    private final ProductPackageClientAdapter productPackageClientAdapter;
    private final ExchangeRateService exchangeRateService;

    @Override
    public List<ProductPackageInstant> getAll(@NonNull Currency currency) {
        List<ProductPackage> productPackages = productPackageClientAdapter.getAllProductPackages();
        log.info("Found product packages, size: {}", productPackages.size());

        return productPackages.stream()
                .map(p -> toProductPackageInstant(p, currency))
                .collect(Collectors.toList());
    }

    @Override
    public ProductPackageInstant getById(@NonNull String id, @NonNull Currency currency) {
        ProductPackage productPackage = productPackageClientAdapter.getById(id);

        if (productPackage != null) {
            log.info("Product package found: {}", productPackage);
            return toProductPackageInstant(productPackage, currency);
        }

        logNothingFound(id);

        return null;
    }

    @Override
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

    @Override
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

            // At this point, product must exist
            return productPackageInstant.getProducts().stream()
                    .filter(product -> product.getId().equals(productId))
                    .findFirst()
                    .get();
        }

        return null;
    }

    @Override
    public boolean deleteById(@NonNull String id) {
        ProductPackage productPackage = productPackageClientAdapter.deleteById(id);

        if (productPackage != null) {
            log.info("Removed product package, id: {}", id);
            return true;
        }

        log.info("Could not remove product package, id not found: {}", id);

        return false;
    }

    @Override
    public ProductPackageInstant save(
            @NonNull ProductPackage productPackage,
            @NonNull Currency currency) {
        productPackageClientAdapter.save(productPackage);

        return toProductPackageInstant(productPackage, currency);
    }

    // TODO: Pricing concerns and conversions should probably be in a separate class since you don't necessarily
    // need to do an exchange unless you're displaying to the customer
    private ProductPackageInstant toProductPackageInstant(ProductPackage productPackage, Currency localCurrency) {
        List<Product> products = productPackage.getProductIds().stream()
                .map(productId -> productService.getById(productId))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (products.size() != productPackage.getProductIds().size()) {
            throw new IllegalStateException("Product package has products which cannot be found, id: " + productPackage.getId());
        }

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
