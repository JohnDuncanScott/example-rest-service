package com.idm.service.adapters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.idm.service.models.data.Product;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: This should be replaced by the real backend service
@Component
@Slf4j
public class ProductClientAdapterImpl implements ProductClientAdapter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    // TODO: Move cache to parent service. Should be more like exchange rate one
    private final Map<String, Product> productCache = new HashMap<>();

    @Inject
    private CloseableHttpClient httpClient;

    @Override
    public List<Product> getAllProducts() {
        refreshCache();
        return ImmutableList.copyOf(productCache.values());
    }

    @Override
    public Product getById(@NonNull String id) {
        refreshCache();
        return productCache.get(id);
    }

    public void refreshCache() {
        // TODO: Need TTL for cache so will expire at some point (think there's one in Guava)
        if (productCache.size() > 0) {
            log.info("Products already cached, not calling Products service");
            return;
        }

        HttpGet httpGet = new HttpGet("https://product-service.herokuapp.com/api/v1/products");
        // TODO: Move credentials to secure store
        String base64EncodedCredentials = "Basic " + Base64.getEncoder().encodeToString(("user:pass").getBytes());
        httpGet.setHeader("Authorization", base64EncodedCredentials);
        httpGet.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            log.info("Products service response status: {}", response.getStatusLine());

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                log.info("Products service response content:\n{}", json);
                List<Product> products = OBJECT_MAPPER.readValue(json, new TypeReference<List<Product>>() { });

                for (Product product : products) {
                    productCache.put(product.getId(), product);
                }
            }
        } catch (IOException ex) {
            log.error("Failed while calling Products service", ex);
        }
    }
}
