package com.idm.service.adapters;

import com.idm.service.assertions.AssertMessages;
import com.idm.service.models.data.ProductPackage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

// These tests are just to make sure the fake adapter is working correctly. Some have state, which is bad, many will
// be deleted when a real dependency is added. They are also not quite as thorough as they could be
class ProductPackageClientAdapterImplTest {
    private static final String ID = "1";
    private ProductPackageClientAdapter productPackageClientAdapter = new ProductPackageClientAdapterImpl();

    @Test
    void getAllProductPackagesReturnsAllData() {
        List<ProductPackage> productPackages = productPackageClientAdapter.getAllProductPackages();

        assertThat(productPackages, hasSize(3));
    }

    @Test
    void getByIdReturnsCorrectPackage() {
        ProductPackage productPackage = productPackageClientAdapter.getById(ID);

        assertThat(productPackage, notNullValue());
        assertThat(productPackage.getId(), equalTo(ID));
    }

    @Test
    void getByIdThrowsOnNullId() {
        AssertMessages.assertThatLombokNonNullIsPresent(() -> productPackageClientAdapter.getById(null), "id");
    }

    // This is a bad stateful test but will be removed when a real service call is done.
    @Test
    void deleteAndSaveWorkCorrectly() {
        // Delete deletes package
        ProductPackage productPackage = productPackageClientAdapter.deleteById(ID);

        assertThat(productPackage, notNullValue());
        assertThat(productPackage.getId(), equalTo(ID));
        assertThat(productPackageClientAdapter.getById(ID), nullValue());

        // Save saves package that was removed
        productPackage = productPackageClientAdapter.save(productPackage);

        assertThat(productPackage, notNullValue());
        assertThat(productPackage.getId(), equalTo(ID));
        assertThat(productPackageClientAdapter.getById(ID), notNullValue());
    }

    @Test
    void deleteReturnsNullForUnknownPackage() {
        ProductPackage productPackage = productPackageClientAdapter.deleteById("unknownId");

        assertThat(productPackage, nullValue());
    }

    @Test
    void deleteThrowsOnNullId() {
        AssertMessages.assertThatLombokNonNullIsPresent(() -> productPackageClientAdapter.deleteById(null), "id");
    }

    // This is a bad stateful test but will be removed when a real service call is done.
    @Test
    void saveStoresANewPackageWithNullId() {
        ProductPackage newProductPackage = new ProductPackage("name", "description");

        ProductPackage actualProductPackage = productPackageClientAdapter.save(newProductPackage);

        try {
            assertThat(actualProductPackage, notNullValue());
            assertThat("Id should have changed", actualProductPackage.getId(), not(equalTo(newProductPackage.getId())));
            assertThat("Id should be new", actualProductPackage.getId(), equalTo("4"));
        } finally {
            // Restore state
            productPackageClientAdapter.deleteById(actualProductPackage.getId());
        }
    }

    // This is a bad stateful test but will be removed when a real service call is done.
    @Test
    void saveStoresAnUpdatedPackage() {
        final String newName = "newName";
        ProductPackage originalProductPackage = productPackageClientAdapter.getById(ID);
        ProductPackage updatedProductPackage = new ProductPackage(
                originalProductPackage.getId(),
                newName,
                originalProductPackage.getDescription(),
                originalProductPackage.getProductIds()
        );

        ProductPackage actualProductPackage = productPackageClientAdapter.save(updatedProductPackage);

        try {
            assertThat(actualProductPackage, notNullValue());
            assertThat(
                    "Name should have changed",
                    actualProductPackage.getName(),
                    not(equalTo(originalProductPackage.getName())));
            assertThat(
                    "Name should be one specified",
                    actualProductPackage.getName(),
                    equalTo(newName));
            assertThat(
                    "Rest of properties should be correct",
                    productPackageClientAdapter.getById(originalProductPackage.getId()),
                    equalTo(updatedProductPackage));
        } finally {
            // Restore state
            productPackageClientAdapter.save(originalProductPackage);
        }
    }
}