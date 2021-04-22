package com.idm.service.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.idm.service.adapters.ProductPackageClientAdapter;
import com.idm.service.assertions.AssertMessages;
import com.idm.service.models.data.Product;
import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductPackageInstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductPackageServiceImplTest {
    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency GBP = Currency.getInstance("GBP");
    private static final BigDecimal USD_EXCHANGE_RATE = BigDecimal.ONE;
    private static final BigDecimal GBP_EXCHANGE_RATE = BigDecimal.TEN;
    private static final Product PRODUCT1 = new Product("productId1", "productName1", BigDecimal.TEN);
    private static final Product PRODUCT2 = new Product("productId2", "productName2", BigDecimal.TEN);
    private static final List<Product> PRODUCTS = ImmutableList.of(PRODUCT1, PRODUCT2);
    private static final ProductPackage PRODUCT_PACKAGE = new ProductPackage(
            "packageId",
            "packageName",
            "packageDescription",
            ImmutableSet.of(PRODUCT1.getId(), PRODUCT2.getId()));
    private ProductPackageService productPackageService;

    @Mock
    private ProductService productService;
    @Mock
    private ProductPackageClientAdapter productPackageClientAdapter;
    @Mock
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        lenient()
                .when(productService.getById(PRODUCT1.getId())).thenReturn(PRODUCT1);
        lenient()
                .when(productService.getById(PRODUCT2.getId())).thenReturn(PRODUCT2);
        lenient()
                .when(productPackageClientAdapter.getAllProductPackages())
                .thenReturn(ImmutableList.of(PRODUCT_PACKAGE));
        lenient()
                .when(productPackageClientAdapter.getById(PRODUCT_PACKAGE.getId()))
                .thenReturn(PRODUCT_PACKAGE);
        lenient()
                .when(productPackageClientAdapter.save(PRODUCT_PACKAGE))
                .thenReturn(PRODUCT_PACKAGE);
        lenient()
                .when(exchangeRateService.getExchangeRateForUsdTo(USD))
                .thenReturn(USD_EXCHANGE_RATE);
        lenient()
                .when(exchangeRateService.getExchangeRateForUsdTo(GBP))
                .thenReturn(GBP_EXCHANGE_RATE);
        productPackageService = new ProductPackageServiceImpl(
                productService,
                productPackageClientAdapter,
                exchangeRateService);
    }

    @Test
    void getAllReturnsAllPackages() {
        List<ProductPackageInstant> productPackageInstants = productPackageService.getAll(USD);

        assertThat(productPackageInstants, hasSize(1));
    }

    @Test
    void getAllThrowsOnNullCurrency() {
        AssertMessages.assertThatLombokNonNullIsPresent(() -> productPackageService.getAll(null), "currency");
    }

    @Test
    void getByIdReturnsConvertedProductPackage() {
        ProductPackageInstant productPackageInstant = productPackageService.getById(PRODUCT_PACKAGE.getId(), USD);

        assertThat(productPackageInstant, notNullValue());
        assertThat("Product package should match", productPackageInstant.getProductPackage(), equalTo(PRODUCT_PACKAGE));
        assertThat("Products should match", productPackageInstant.getProducts(), equalTo(PRODUCTS));
        assertThat(
                "Total price should be correct",
                productPackageInstant.getTotalUsdPrice(),
                equalTo(PRODUCT1.getUsdPrice().add(PRODUCT2.getUsdPrice())));
        assertThat(
                "Local currency should be correct",
                productPackageInstant.getLocalCurrency(),
                equalTo(USD));
        assertThat(
                "Local exchange rate should be correct",
                productPackageInstant.getLocalExchangeRate(),
                equalTo(USD_EXCHANGE_RATE));
        assertThat(
                "Timestamp should be older",
                productPackageInstant.getExchangeRateRetrievalTime(),
                lessThanOrEqualTo(Instant.now()));
    }

    @Test
    void getByIdConvertsAccordingToCurrency() {
        ProductPackageInstant productPackageInstant = productPackageService.getById(PRODUCT_PACKAGE.getId(), GBP);

        assertThat(productPackageInstant, notNullValue());
        assertThat(
                "Local currency should be correct",
                productPackageInstant.getLocalCurrency(),
                equalTo(GBP));
        assertThat(
                "Local exchange rate should be correct",
                productPackageInstant.getLocalExchangeRate(),
                equalTo(GBP_EXCHANGE_RATE));
    }

    @Test
    void getByIdReturnsNullForUnknownId() {
        ProductPackageInstant productPackageInstant = productPackageService.getById("unknownId", USD);

        assertThat(productPackageInstant, nullValue());
    }

    @Test
    void getByIdThrowsOnInvalidProductMapping() {
        when(productService.getById(PRODUCT1.getId())).thenReturn(null);

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> productPackageService.getById(PRODUCT_PACKAGE.getId(), USD));

        assertThat(
                "Error message is incorrect",
                thrown.getMessage(),
                equalTo("Product package has products which cannot be found, id: " + PRODUCT_PACKAGE.getId()));
    }

    @Test
    void getByIdThrowsOnNullId() {
        AssertMessages.assertThatLombokNonNullIsPresent(() -> productPackageService.getById(null, USD), "id");
    }

    @Test
    void getByIdThrowsOnNullCurrency() {
        AssertMessages.assertThatLombokNonNullIsPresent(() -> productPackageService.getById("", null), "currency");
    }

    @Test
    void getProductsForPackageReturnsCorrectProducts() {
        List<Product> products = productPackageService.getProductsForPackage(PRODUCT_PACKAGE.getId(), USD);

        assertThat(products, notNullValue());
        assertThat(products, equalTo(PRODUCTS));
    }

    @Test
    void getProductsForPackageReturnsEmptyListForUnknownId() {
        List<Product> products = productPackageService.getProductsForPackage("unknownId", USD);

        assertThat(products, notNullValue());
        assertThat(products, equalTo(Collections.EMPTY_LIST));
    }

    @Test
    void getProductsForPackageThrowsOnNullId() {
        AssertMessages.assertThatLombokNonNullIsPresent(
                () -> productPackageService.getProductsForPackage(null, USD),
                "packageId");
    }

    @Test
    void getProductsForPackageThrowsOnNullCurrency() {
        AssertMessages.assertThatLombokNonNullIsPresent(
                () -> productPackageService.getProductsForPackage("", null),
                "currency");
    }

    @Test
    void getProductForPackageReturnsCorrectProduct() {
        Product product = productPackageService.getProductForPackage(PRODUCT_PACKAGE.getId(), PRODUCT1.getId(), USD);

        assertThat(product, notNullValue());
        assertThat(product, equalTo(PRODUCT1));
    }

    @Test
    void getProductForPackageReturnsNullForUnknownProduct() {
        Product product = productPackageService.getProductForPackage(PRODUCT_PACKAGE.getId(), "unknownId", USD);

        assertThat(product, nullValue());
    }

    @Test
    void getProductForPackageReturnsNullForUnknownPackage() {
        Product product = productPackageService.getProductForPackage("unknownId", PRODUCT1.getId(), USD);

        assertThat(product, nullValue());
    }

    @Test
    void getProductForPackageThrowsOnNullPackageId() {
        AssertMessages.assertThatLombokNonNullIsPresent(
                () -> productPackageService.getProductForPackage(null, "", USD),
                "packageId");
    }

    @Test
    void getProductForPackageThrowsOnNullProductId() {
        AssertMessages.assertThatLombokNonNullIsPresent(
                () -> productPackageService.getProductForPackage("", null, USD),
                "productId");
    }

    @Test
    void getProductForPackageThrowsOnNullCurrency() {
        AssertMessages.assertThatLombokNonNullIsPresent(
                () -> productPackageService.getProductForPackage("", "", null),
                "currency");
    }

    @Test
    void deleteByIdReturnsTrueForFoundPackage() {
        when(productPackageClientAdapter.deleteById(PRODUCT_PACKAGE.getId())).thenReturn(PRODUCT_PACKAGE);

        assertThat("Found package should return true", productPackageService.deleteById(PRODUCT_PACKAGE.getId()));
    }

    @Test
    void deleteByIdReturnsFalseForUnknownPackage() {
        assertThat("Found package should return false", !productPackageService.deleteById(PRODUCT_PACKAGE.getId()));
    }

    @Test
    void deleteByIdThrowsOnNullId() {
        AssertMessages.assertThatLombokNonNullIsPresent(() -> productPackageService.deleteById(null), "id");
    }

    @Test
    void saveStoresPackageViaClient() {
        ProductPackageInstant productPackageInstant = productPackageService.save(PRODUCT_PACKAGE, USD);

        assertThat(productPackageInstant, notNullValue());
        verify(productPackageClientAdapter).save(PRODUCT_PACKAGE);
    }

    @Test
    void saveThrowsOnNullPackage() {
        AssertMessages.assertThatLombokNonNullIsPresent(() -> productPackageService.save(null, USD), "productPackage");
    }

    @Test
    void saveThrowsOnNullCurrency() {
        AssertMessages.assertThatLombokNonNullIsPresent(() -> productPackageService.save(PRODUCT_PACKAGE, null), "currency");
    }
}