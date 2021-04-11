package com.idm.service.controllers;

import com.idm.service.assemblers.ProductPackageResourceAssembler;
import com.idm.service.models.data.Product;
import com.idm.service.models.data.ProductPackage;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO: Fix not having proper error page
@Slf4j
@CrossOrigin(origins = { "http://localhost:8081" })
@RestController
@RequestMapping(value = "/packages", produces = { "application/hal+json" })
public class ProductPackageController {
    @Autowired
    private ProductPackageService productPackageService;

    private ProductPackageResourceAssembler productPackageResourceAssembler = new ProductPackageResourceAssembler();

    @GetMapping()
    public ResponseEntity<CollectionModel<ProductPackageResource>> getAllProductPackages() {
        List<ProductPackage> productPackages = productPackageService.getAll();

        Link selfLink = linkTo(methodOn(getClass()).getAllProductPackages()).withSelfRel();
        CollectionModel<ProductPackageResource> productPackageResources = productPackageResourceAssembler
                .toCollectionModel(productPackages);
        productPackageResources.add(selfLink);

        return ResponseEntity.ok(productPackageResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPackageResource> getProductPackage(@PathVariable String id) {
        ProductPackage productPackage = productPackageService.getById(id);

        if (productPackage != null) {
            ProductPackageResource productPackageResource = productPackageResourceAssembler.toModel(productPackage);
            return ResponseEntity.ok(productPackageResource);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{packageId}/products")
    public ResponseEntity<CollectionModel<EntityModel>> getProductsForPackage(@PathVariable String packageId) {
        List<Product> productsForPackage = productPackageService.getProductsForPackage(packageId);
        List<EntityModel> productResources = new ArrayList<>();

        for (final Product product : productsForPackage) {
            EntityModel<Product> productResource = EntityModel.of(product);
            productResources.add(productResource);
            Link selfLink = linkTo(methodOn(getClass())
                    .getProductForPackage(packageId, product.getId())).withSelfRel();
            productResource.add(selfLink);
        }

        Link selfLink = linkTo(methodOn(getClass())
                .getProductsForPackage(packageId)).withSelfRel();
        CollectionModel<EntityModel> productResourceCollection = CollectionModel.of(productResources, selfLink);

        return ResponseEntity.ok(productResourceCollection);
    }

    @GetMapping("/{packageId}/products/{productId}")
    public ResponseEntity<EntityModel<Product>> getProductForPackage(
            @PathVariable String packageId,
            @PathVariable String productId) {
        Product productForPackage = productPackageService.getProductForPackage(packageId, productId);

        if (productForPackage != null) {
            EntityModel<Product> productEntityModel = EntityModel.of(productForPackage);
            Link selfLink = linkTo(methodOn(getClass())
                    .getProductForPackage(packageId, productId)).withSelfRel();
            productEntityModel.add(selfLink);
            return ResponseEntity.ok(productEntityModel);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductPackage(@PathVariable String id) {
        ProductPackage productPackage = productPackageService.deleteById(id);

        if (productPackage != null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductPackageResource> updateProductPackageMetadata(
            @PathVariable String id,
            @RequestBody ProductPackageResource productPackageResource) {
        log.info("Received product package resource: {}", productPackageResource);
        ProductPackage productPackage = productPackageResourceAssembler.fromModel(
                productPackageResource,
                productPackageService.getById(id)
        );
        ProductPackage updatedProductPackage = productPackageService.save(productPackage);
        ProductPackageResource updatedProductPackageResource = productPackageResourceAssembler
                .toModel(updatedProductPackage);

        return ResponseEntity.ok(updatedProductPackageResource);
    }

    @PostMapping()
    public ResponseEntity<ProductPackageResource> createProductPackage(
            @RequestBody ProductPackageResource productPackageResource) {
        log.info("Received product package resource: {}", productPackageResource);
        ProductPackage productPackage = productPackageResourceAssembler.fromModel(productPackageResource);
        ProductPackage createdProductPackage = productPackageService.save(productPackage);
        ProductPackageResource createdProductPackageResource = productPackageResourceAssembler
                .toModel(createdProductPackage);
        URI uri = MvcUriComponentsBuilder
                .fromController(getClass())
                .path("/{id}")
                .buildAndExpand(createdProductPackageResource.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdProductPackageResource);
    }
}
