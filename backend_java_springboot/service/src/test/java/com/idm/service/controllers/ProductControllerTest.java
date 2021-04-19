package com.idm.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.idm.service.models.data.Product;
import com.idm.service.services.ProductService;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Basic tests for now until make controller fully HATEOS compliant
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final Product PRODUCT = new Product("productId", "productName", BigDecimal.TEN);
    private static final List<Product> PRODUCTS = ImmutableList.of(PRODUCT);

    @Inject
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProductsReturnsAllProducts() throws Exception {
        when(productService.getAll()).thenReturn(PRODUCTS);

        ResultActions resultActions = mockMvc
                .perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
        assertThatProductIsCorrect(resultActions, "$[0]", PRODUCT);
    }

    @Test
    void getProductReturnsCorrectProduct() throws Exception {
        when(productService.getById(PRODUCT.getId())).thenReturn(PRODUCT);

        ResultActions resultActions = mockMvc
                .perform(get("/products/" + PRODUCT.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        assertThatProductIsCorrect(resultActions, "$", PRODUCT);
    }

    private void assertThatProductIsCorrect(
            ResultActions resultActions,
            String jsonExpressionPrefix,
            Product expectedProduct) throws Exception {
        resultActions
                .andExpect(jsonPath(jsonExpressionPrefix + ".id", equalTo(expectedProduct.getId())))
                .andExpect(jsonPath(jsonExpressionPrefix + ".name", equalTo(expectedProduct.getName())))
                .andExpect(jsonPath(jsonExpressionPrefix + ".usdPrice", equalTo(10)));
    }
}