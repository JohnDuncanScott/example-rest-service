package com.idm.service.assemblers;

import com.idm.service.controllers.ProductPackageController;
import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductPackageInstant;
import com.idm.service.models.resources.ProductPackageResource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import javax.annotation.Nullable;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
public class ProductPackageResourceAssembler
        extends RepresentationModelAssemblerSupport<ProductPackageInstant, ProductPackageResource> {
    public ProductPackageResourceAssembler() {
        super(ProductPackageController.class, ProductPackageResource.class);
    }

    public ProductPackage fromModel(@NonNull ProductPackageResource productPackageResource) {
        return fromModel(productPackageResource, null);
    }

    // TODO: Not sure if architecturally this is the right place for it, but it's easy to find and with other similar
    // methods. Should maybe be in "ProductPackageAssembler" although that might be overkill
    // Will handle both new objects that need creating and transferring data from existing ones
    public ProductPackage fromModel(
            @NonNull ProductPackageResource productPackageResource,
            @Nullable ProductPackageInstant originalProductPackageInstant) {
        boolean isOriginalProductPackagePresent = originalProductPackageInstant != null;

        if (!isOriginalProductPackagePresent && productPackageResource.getId() != null) {
            log.info(
                    "Product package id '{}' not found, but was supplied, ignoring id so it can be generated later",
                    productPackageResource.getId());
        }

        if (isOriginalProductPackagePresent) {
            return new ProductPackage(
                    originalProductPackageInstant.getProductPackage().getId(),
                    productPackageResource.getName(),
                    productPackageResource.getDescription(),
                    originalProductPackageInstant.getProductPackage().getProductIds()
            );
        } else {
            return new ProductPackage(
                    productPackageResource.getName(),
                    productPackageResource.getDescription()
            );
        }
    }

    @Override
    public ProductPackageResource toModel(@NonNull ProductPackageInstant productPackageInstant) {
        ProductPackageResource resource = this.createModelWithId(
                productPackageInstant.getProductPackage().getId(), productPackageInstant);
        Link productsLink = linkTo(methodOn(ProductPackageController.class)
                .getProductsForPackage(
                        productPackageInstant.getProductPackage().getId(),
                        productPackageInstant.getLocalCurrency().getCurrencyCode())).withRel("products");
        resource.add(productsLink);

        return resource;
    }

    @Override
    protected ProductPackageResource instantiateModel(ProductPackageInstant productPackageInstant) {
        ProductPackage productPackage = productPackageInstant.getProductPackage();

        return new ProductPackageResource(
                productPackage.getId(),
                productPackage.getName(),
                productPackage.getDescription(),
                productPackageInstant.getLocalCurrency(),
                productPackageInstant.getTotalLocalPrice());
    }
}
