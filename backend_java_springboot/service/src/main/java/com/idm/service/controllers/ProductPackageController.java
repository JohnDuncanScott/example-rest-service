package com.idm.service.controllers;

import com.idm.service.assemblers.ProductPackageResourceAssembler;
import com.idm.service.models.data.Product;
import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductPackageInstant;
import com.idm.service.models.resources.ProductPackageResource;
import com.idm.service.services.ProductPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO: Fix not having proper error page
@Slf4j
@CrossOrigin(origins = { "http://localhost:8081" })
@RestController
@RequestMapping(value = "/packages", produces = { "application/hal+json" })
public class ProductPackageController {
    @Inject
    private ProductPackageService productPackageService;

    private ProductPackageResourceAssembler productPackageResourceAssembler = new ProductPackageResourceAssembler();

    @GetMapping()
    public ResponseEntity<CollectionModel<ProductPackageResource>> getAllProductPackages(
            @RequestParam(required = false) String cs) {
        // TODO: Get spring to auto-convert currency for me
        Currency currency = toCurrency(cs);
        List<ProductPackageInstant> productPackageInstants = productPackageService.getAll(currency);

        Link selfLink = linkTo(methodOn(getClass()).getAllProductPackages(currency.getCurrencyCode())).withSelfRel();
        CollectionModel<ProductPackageResource> productPackageResources = productPackageResourceAssembler
                .toCollectionModel(productPackageInstants);
        productPackageResources.add(selfLink);

        return ResponseEntity.ok(productPackageResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPackageResource> getProductPackage(
            @PathVariable String id,
            @RequestParam(required = false) String cs) {
        Currency currency = toCurrency(cs);
        ProductPackageInstant productPackageInstant = productPackageService.getById(id, currency);

        if (productPackageInstant != null) {
            ProductPackageResource productPackageResource = productPackageResourceAssembler
                    .toModel(productPackageInstant);
            return ResponseEntity.ok(productPackageResource);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{packageId}/products")
    public ResponseEntity<CollectionModel<EntityModel>> getProductsForPackage(
            @PathVariable String packageId,
            @RequestParam(required = false) String cs) {
        Currency currency = toCurrency(cs);
        List<Product> productsForPackage = productPackageService.getProductsForPackage(packageId, currency);
        List<EntityModel> productResources = new ArrayList<>();

        for (final Product product : productsForPackage) {
            EntityModel<Product> productResource = EntityModel.of(product);
            productResources.add(productResource);
            Link selfLink = linkTo(methodOn(getClass())
                    .getProductForPackage(packageId, product.getId(), currency.getCurrencyCode())).withSelfRel();
            productResource.add(selfLink);
        }

        Link selfLink = linkTo(methodOn(getClass())
                .getProductsForPackage(packageId, currency.getCurrencyCode())).withSelfRel();
        CollectionModel<EntityModel> productResourceCollection = CollectionModel.of(productResources, selfLink);

        return ResponseEntity.ok(productResourceCollection);
    }

    @GetMapping("/{packageId}/products/{productId}")
    public ResponseEntity<EntityModel<Product>> getProductForPackage(
            @PathVariable String packageId,
            @PathVariable String productId,
            @RequestParam(required = false) String cs) {
        Currency currency = toCurrency(cs);
        Product productForPackage = productPackageService.getProductForPackage(packageId, productId, currency);

        if (productForPackage != null) {
            EntityModel<Product> productEntityModel = EntityModel.of(productForPackage);
            Link selfLink = linkTo(methodOn(getClass())
                    .getProductForPackage(packageId, productId, currency.getCurrencyCode())).withSelfRel();
            productEntityModel.add(selfLink);
            return ResponseEntity.ok(productEntityModel);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductPackage(@PathVariable String id) {
        if (productPackageService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductPackageResource> updateProductPackageMetadata(
            @PathVariable String id,
            @RequestBody ProductPackageResource productPackageResource,
            @RequestParam(required = false) String cs) {
        log.info("Received product package resource: {}", productPackageResource);
        Currency currency = toCurrency(cs);
        ProductPackage productPackage = productPackageResourceAssembler.fromModel(
                productPackageResource,
                productPackageService.getById(id, currency)
        );
        ProductPackageInstant updatedProductPackageInstant = productPackageService.save(productPackage, currency);
        ProductPackageResource updatedProductPackageResource = productPackageResourceAssembler
                .toModel(updatedProductPackageInstant);

        return ResponseEntity.ok(updatedProductPackageResource);
    }

    @PostMapping()
    public ResponseEntity<ProductPackageResource> createProductPackage(
            @RequestBody ProductPackageResource productPackageResource,
            @RequestParam(required = false) String cs) {
        log.info("Received product package resource: {}", productPackageResource);
        Currency currency = toCurrency(cs);
        ProductPackage productPackage = productPackageResourceAssembler.fromModel(productPackageResource);
        ProductPackageInstant createdProductPackageInstant = productPackageService.save(productPackage, currency);
        ProductPackageResource createdProductPackageResource = productPackageResourceAssembler
                .toModel(createdProductPackageInstant);
        URI uri = MvcUriComponentsBuilder
                .fromController(getClass())
                .path("/{id}")
                .buildAndExpand(createdProductPackageResource.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdProductPackageResource);
    }

    private Currency toCurrency(String currency) {
        return currency != null ? Currency.getInstance(currency.toUpperCase()) : Currency.getInstance("USD");
    }
}
