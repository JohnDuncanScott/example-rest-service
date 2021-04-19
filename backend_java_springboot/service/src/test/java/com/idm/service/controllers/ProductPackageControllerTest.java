package com.idm.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.idm.service.models.data.Product;
import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductPackageInstant;
import com.idm.service.models.resources.ProductPackageResource;
import com.idm.service.services.ProductPackageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductPackageController.class)
class ProductPackageControllerTest {
    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency GBP = Currency.getInstance("GBP");
    private static final String PRODUCT_ID = "productId";
    private static final Product PRODUCT = new Product(PRODUCT_ID, "productName", BigDecimal.TEN);
    private static final List<Product> PRODUCTS = ImmutableList.of(PRODUCT);
    private static final String PACKAGE_ID = "packageId";
    private static final ProductPackage PRODUCT_PACKAGE = new ProductPackage(
            PACKAGE_ID,
            "packageName",
            "packageDescription",
            ImmutableSet.of(PRODUCT_ID));
    private static final ProductPackageInstant PRODUCT_PACKAGE_INSTANT_USD = new ProductPackageInstant(
            PRODUCT_PACKAGE,
            PRODUCTS,
            BigDecimal.TEN,
            USD,
            BigDecimal.ONE,
            Instant.now());
    private static final ProductPackageInstant PRODUCT_PACKAGE_INSTANT_GBP = new ProductPackageInstant(
            PRODUCT_PACKAGE,
            PRODUCTS,
            BigDecimal.TEN,
            GBP,
            BigDecimal.ONE,
            Instant.now());
    private static final List<ProductPackageInstant> PRODUCT_PACKAGE_INSTANTS = ImmutableList.of(PRODUCT_PACKAGE_INSTANT_USD);
    private static final ProductPackageResource PRODUCT_PACKAGE_RESOURCE_USD = new ProductPackageResource(
            null,
            "resourceName",
            "resourceDescription",
            USD,
            BigDecimal.TEN,
            new ArrayList<>());
    private static final ProductPackageResource PRODUCT_PACKAGE_RESOURCE_GBP = new ProductPackageResource(
            null,
            "resourceName",
            "resourceDescription",
            GBP,
            BigDecimal.TEN,
            new ArrayList<>());
    private static final String BASE_URL = "/packages";
    private static final String JSON_PATH_PREFIX_FOR_LIST = "$._embedded";
    private static final String JSON_PATH_PREFIX_FOR_LINKS = "$._links";
    private static final String JSON_PATH_FOR_SELF_LINK = JSON_PATH_PREFIX_FOR_LINKS + ".self.href";
    private static final String PACKAGE_LIST_NAME = "productPackageResourceList";
    private static final String PRODUCT_LIST_NAME = "productList";

    @Inject
    private MockMvc mockMvc;

    @Inject
    private ObjectMapper objectMapper;

    @MockBean
    private ProductPackageService productPackageService;

    @ParameterizedTest
    @CsvSource({
            "/packages?cs=GBP,http://localhost/packages?cs=GBP",
            "/packages/packageId?cs=GBP,http://localhost/packages/packageId?cs=GBP",
            "/packages/packageId/products?cs=GBP,http://localhost/packages/packageId/products?cs=GBP",
            "/packages/packageId/products/productId?cs=GBP,http://localhost/packages/packageId/products/productId?cs=GBP"
    })
    void getMethodsReturnOkAndHalCompliantAndUseSpecifiedCurrency(
            String url,
            String expectedSelfLink) throws Exception {
        lenient().when(productPackageService.getById(PACKAGE_ID, GBP)).thenReturn(PRODUCT_PACKAGE_INSTANT_GBP);
        lenient().when(productPackageService.getProductsForPackage(PACKAGE_ID, GBP)).thenReturn(PRODUCTS);
        lenient().when(productPackageService.getProductForPackage(PACKAGE_ID, PRODUCT_ID, GBP)).thenReturn(PRODUCT);

        ResultActions resultActions = mockMvc
                .perform(get(url))
                .andDo(print())
                .andExpect(status().isOk());
        assertThatSelfLinkIsCorrect(resultActions, expectedSelfLink);
    }

    @ParameterizedTest
    @CsvSource({
            "/packages/packageId",
            "/packages/packageId/products/productId",
    })
    void getMethodsForSpecificResourcesReturnNotFoundWhenMissing(String url) throws Exception {
        mockMvc
            .perform(get(url))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void getAllProductPackagesIsHalCompliant() throws Exception {
        when(productPackageService.getAll(USD)).thenReturn(PRODUCT_PACKAGE_INSTANTS);

        ResultActions resultActions = mockMvc
                .perform(get(BASE_URL))
                .andDo(print());

        assertThatListIsHalCompliant(
                resultActions,
                PACKAGE_LIST_NAME,
                "http://localhost/packages?cs=USD");
    }

    @Test
    void getAllProductPackagesReturnsCorrectContents() throws Exception {
        when(productPackageService.getAll(USD)).thenReturn(PRODUCT_PACKAGE_INSTANTS);
        final String jsonListPath = getJsonPathForList(PACKAGE_LIST_NAME);

        mockMvc
                .perform(get(BASE_URL))
                .andDo(print())
                .andExpect(jsonPath(jsonListPath, hasSize(1)))
                .andExpect(jsonPath(jsonListPath + "[0].id", equalTo(PACKAGE_ID)));
    }

    @Test
    void getProductPackageReturnsCorrectContents() throws Exception {
        when(productPackageService.getById(PACKAGE_ID, USD)).thenReturn(PRODUCT_PACKAGE_INSTANT_USD);

        mockMvc
                .perform(get(BASE_URL + "/" + PACKAGE_ID))
                .andDo(print())
                .andExpect(jsonPath("$.id", equalTo(PACKAGE_ID)));
    }

    @Test
    void getProductsForPackageIsHalCompliant() throws Exception {
        when(productPackageService.getProductsForPackage(PACKAGE_ID, USD)).thenReturn(PRODUCTS);

        ResultActions resultActions = mockMvc
                .perform(get(String.format("%s/%s/products", BASE_URL, PACKAGE_ID)))
                .andDo(print());

        assertThatListIsHalCompliant(
                resultActions,
                PRODUCT_LIST_NAME,
                "http://localhost/packages/packageId/products?cs=USD");
    }

    @Test
    void getProductsForPackageReturnsCorrectContents() throws Exception {
        when(productPackageService.getProductsForPackage(PACKAGE_ID, USD)).thenReturn(PRODUCTS);
        final String jsonListPath = getJsonPathForList(PRODUCT_LIST_NAME);

        mockMvc
                .perform(get(String.format("%s/%s/products", BASE_URL, PACKAGE_ID)))
                .andDo(print())
                .andExpect(jsonPath(jsonListPath, hasSize(1)))
                .andExpect(jsonPath(jsonListPath + "[0].id", equalTo(PRODUCT_ID)));
    }

    @Test
    void getProductForPackageReturnsCorrectContents() throws Exception {
        when(productPackageService.getProductForPackage(PACKAGE_ID, PRODUCT_ID, USD)).thenReturn(PRODUCT);

        mockMvc
                .perform(get(String.format("%s/%s/products/%s", BASE_URL, PACKAGE_ID, PRODUCT_ID)))
                .andDo(print())
                .andExpect(jsonPath("$.id", equalTo(PRODUCT_ID)));
    }

    @Test
    void deleteProductPackageReturnsNoContentWhenFound() throws Exception {
        when(productPackageService.deleteById(PACKAGE_ID)).thenReturn(true);

        mockMvc
                .perform(delete(BASE_URL + "/" + PACKAGE_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProductPackageReturnsNotFoundWhenMissing() throws Exception {
        when(productPackageService.deleteById(PACKAGE_ID)).thenReturn(false);

        mockMvc
                .perform(delete(BASE_URL + "/" + PACKAGE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProductPackageMetadataReturnsOk() throws Exception {
        when(productPackageService.save(any(), eq(USD))).thenReturn(PRODUCT_PACKAGE_INSTANT_USD);

        mockMvc
                .perform(
                        put(BASE_URL + "/" + PACKAGE_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT_PACKAGE_RESOURCE_USD)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateProductPackageMetadataUsesSpecifiedCurrency() throws Exception {
        when(productPackageService.save(any(), eq(GBP))).thenReturn(PRODUCT_PACKAGE_INSTANT_GBP);

        ResultActions resultActions = mockMvc
                .perform(
                        put(BASE_URL + "/" + PACKAGE_ID + "?cs=GBP")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT_PACKAGE_RESOURCE_GBP)))
                .andDo(print())
                .andExpect(status().isOk());
        assertThatSelfLinkIsCorrect(resultActions, "http://localhost/packages/packageId?cs=GBP");
    }

    @Test
    void createProductPackageReturnsCreated() throws Exception {
        when(productPackageService.save(any(), eq(USD))).thenReturn(PRODUCT_PACKAGE_INSTANT_USD);

        mockMvc
                .perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT_PACKAGE_RESOURCE_USD)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createProductPackageUsesSpecifiedCurrency() throws Exception {
        when(productPackageService.save(any(), eq(GBP))).thenReturn(PRODUCT_PACKAGE_INSTANT_GBP);

        ResultActions resultActions = mockMvc
                .perform(
                        post(BASE_URL + "?cs=GBP")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT_PACKAGE_RESOURCE_GBP)))
                .andDo(print())
                .andExpect(status().isCreated());
        assertThatSelfLinkIsCorrect(resultActions, "http://localhost/packages/packageId?cs=GBP");
    }

    private void assertThatListIsHalCompliant(
            ResultActions resultActions,
            String listName,
            String expectedSelfLink) throws Exception {
        String jsonPathForList = getJsonPathForList(listName);
        resultActions
                .andExpect(jsonPath(JSON_PATH_PREFIX_FOR_LIST).exists())
                .andExpect(jsonPath(jsonPathForList).exists())
                .andExpect(jsonPath(jsonPathForList).isArray())
                .andExpect(jsonPath(JSON_PATH_PREFIX_FOR_LINKS).exists());
        assertThatSelfLinkIsCorrect(resultActions, expectedSelfLink);
    }

    private void assertThatSelfLinkIsCorrect(ResultActions resultActions, String expectedSelfLink) throws Exception {
        resultActions
            .andExpect(jsonPath(
                JSON_PATH_FOR_SELF_LINK,
                equalTo(expectedSelfLink)));
    }

    private String getJsonPathForList(String listName) {
        return String.format("%s.%s", JSON_PATH_PREFIX_FOR_LIST, listName);
    }
}