package com.idm.service.services;

import com.google.common.collect.ImmutableList;
import com.idm.service.adapters.ProductClientAdapter;
import com.idm.service.assertions.AssertMessages;
import com.idm.service.models.data.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    private static final Product PRODUCT = new Product("productId", "productName", BigDecimal.TEN);
    private ProductService productService;

    @Mock
    private ProductClientAdapter productClientAdapter;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productClientAdapter);
    }

    @Test
    void getAllReturnsAllProducts() {
        when(productClientAdapter.getAllProducts()).thenReturn(ImmutableList.of(PRODUCT));
        List<Product> products = productService.getAll();

        assertThat(products, hasSize(1));
    }

    @Test
    void getByIdThrowsOnNullId() {
        AssertMessages.assertThatLombokNonNullIsPresent(() -> productService.getById(null), "id");
    }

    @Test
    void getByIdReturnsExistingProduct() {
        when(productClientAdapter.getById(PRODUCT.getId())).thenReturn(PRODUCT);
        Product actualProduct = productService.getById(PRODUCT.getId());

        assertThat(actualProduct, notNullValue());
        assertThat("Product should match the one searched for", actualProduct, equalTo(PRODUCT));
    }

    @Test
    void getByIdReturnsNullForMissingProduct() {
        when(productClientAdapter.getById("unknown_id")).thenReturn(null);
        Product actualProduct = productService.getById("unknown_id");

        assertThat(actualProduct, nullValue());
    }
}