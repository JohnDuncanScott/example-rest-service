package com.idm.service.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.idm.service.models.data.Product;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
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

        // HACK: This service no longer exists so create products locally
        final List<Product> products = ImmutableList.of(
                new Product("shield", "Shield", "199"),
                new Product("helmet", "Helmet", "299"),
                new Product("sword", "Sword", "399"),
                new Product("axe", "Axe", "499"),
                new Product("knife", "Knife", "599"),
                new Product("bow", "Bow", "699"),
                new Product("silver", "Silver", "1000"),
                new Product("gold", "Gold", "2000"),
                new Product("platinum", "Platinum", "4000")
        );

        for (Product product : products) {
            productCache.put(product.getId(), product);
        }

//        HttpGet httpGet = new HttpGet("https://product-service.herokuapp.com/api/v1/products");
//        // TODO: Move credentials to secure store
//        String base64EncodedCredentials = "Basic " + Base64.getEncoder().encodeToString(("user:pass").getBytes());
//        httpGet.setHeader("Authorization", base64EncodedCredentials);
//        httpGet.setHeader("Content-Type", "application/json");
//
//        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
//            log.info("Products service response status: {}", response.getStatusLine());
//
//            if (response.getStatusLine().getStatusCode() == 200) {
//                HttpEntity entity = response.getEntity();
//                String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
//                log.info("Products service response content:\n{}", json);
//                List<Product> products = OBJECT_MAPPER.readValue(json, new TypeReference<List<Product>>() { });
//
//                for (Product product : products) {
//                    productCache.put(product.getId(), product);
//                }
//            } else {
//                throw new IOException("Products service request failed: " + response.getStatusLine());
//            }
//        } catch (IOException ex) {
//            log.error("Failed while calling Products service", ex);
//        }
    }
}
