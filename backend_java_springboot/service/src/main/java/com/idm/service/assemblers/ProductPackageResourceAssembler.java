package com.idm.service.assemblers;

import com.idm.service.controllers.ProductPackageController;
import com.idm.service.models.data.ProductPackage;
import com.idm.service.models.data.ProductPackageInstant;
import com.idm.service.models.resources.ProductPackageResource;
import com.idm.service.models.resources.ProductResource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO: Add full unit tests
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
                    productPackageResource.getProducts().stream().map(p -> p.getId()).collect(Collectors.toSet())
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
        // HACK: Adjust self link for currency (must be a better way of doing this but documentation isn't clear)
        Link selfLink = resource.getLink(IanaLinkRelations.SELF).get();
        UriComponents uc = UriComponentsBuilder.fromUri(selfLink.toUri())
                .queryParam("cs", productPackageInstant.getLocalCurrency().getCurrencyCode())
                .build();
        selfLink = Link.of(uc.toUriString(), IanaLinkRelations.SELF);
        resource.removeLinks().add(selfLink);

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

        // TODO: Should have a separate assembler with their own self links probably?
        List<ProductResource> productResources = productPackageInstant.getProducts().stream()
                .map(p -> new ProductResource(
                        p.getId(),
                        p.getName(),
                        productPackageInstant.getLocalCurrency(),
                        productPackageInstant.getProductLocalPrice(p)))
                .collect(Collectors.toList());

        return new ProductPackageResource(
                productPackage.getId(),
                productPackage.getName(),
                productPackage.getDescription(),
                productPackageInstant.getLocalCurrency(),
                productPackageInstant.getTotalLocalPrice(),
                productResources);
    }
}
