import Vue from 'vue'
import ProductPackageService from '../service/ProductPackageService';
import UserPersonalisationService from '../service/UserPersonalisationService';
import CURRENCY_CODE_KEY from '../service/UserPersonalisationService';
import getSymbolFromCurrency from 'currency-symbol-map'

// TODO: This does nothing but store data. Ideally this would actually be storing basket information
// for the user via another REST service
class BasketService {
    // HACK: Keep state
    // This only stores the metadata. Since the package currency and other state can change, don't store
    // the whole object. Users of this service should get the actual product package data afterwards
    static productPackagesMetadata = {}

    addProductPackage(id) {
        var productPackageMetadata = BasketService.productPackagesMetadata[id];

        if (productPackageMetadata) {
            console.log(`Package already in basket, incrementing count by 1, id: ${id}`);
            BasketService.productPackagesMetadata[id].count = productPackageMetadata.count + 1;
            console.log(`Package count: ${BasketService.productPackagesMetadata[id].count}`);
        } else {
            console.log(`Added package to basket for first time in basket, id: ${id}`);
            var newProductPackageMetadata = {}
            // Makes sure count updating causes reactions
            Vue.set(newProductPackageMetadata, 'count', 1)
            BasketService.productPackagesMetadata[id] = newProductPackageMetadata;
        }
    }

    // Decrements quantity by 1 or removes package completely if only 1 remaining
    removeProductPackage(id) {
        var productPackageMetadata = BasketService.productPackagesMetadata[id];

        if (productPackageMetadata) {
            console.log(`Removing package from basket, id: ${id}`);

            if (productPackageMetadata.count > 1) {
                console.log(`More than 1 in basket, decrementing count by 1, id: ${id}`);
                BasketService.productPackagesMetadata[id].count = productPackageMetadata.count - 1;
            } else {
                this.deleteProductPackage(id);
            }
        } else {
            console.log(`Could not remove package from basket, couldn't find, id: ${id}`);
        }
    }

    // Removes a package regardless of quantity remaining
    deleteProductPackage(id) {
        var productPackageMetadata = BasketService.productPackagesMetadata[id];

        if (productPackageMetadata) {
            console.log(`Deleting package from basket, id: ${id}`);
            // Without this, the basket won't actually remove the final item. See:
            // https://stackoverflow.com/questions/50782129/deleting-an-object-by-key-doesnt-update-the-vue-component
            Vue.delete(BasketService.productPackagesMetadata, id);
        } else {
            console.log(`Could not delete package from basket, couldn't find, id: ${id}`);
        }
    }

    // HACK: Put this here so it's easy to call, uses 2 other services to get right data
    // My promise skills are a little rusty, so the below might not be best practice
    getProductPackageBasketItems() {
            var currencyCode = UserPersonalisationService.getValue(CURRENCY_CODE_KEY);
            var promises = [];
            Object.keys(BasketService.productPackagesMetadata).forEach(key => {
                promises.push(ProductPackageService.getProductPackage(key, currencyCode));
            })
            
            return Promise.all(promises).then(productPackageResponses => {
                var productPackageBasketItems = [];

                productPackageResponses.forEach((res) => {
                    var productPackageBasketItem = {};
                    var metadata = BasketService.productPackagesMetadata[res.data.id];
                    productPackageBasketItem.productPackage = res.data;
                    productPackageBasketItem.count = metadata.count;
                    productPackageBasketItems.push(productPackageBasketItem);
                });

                return productPackageBasketItems;
            });
    }

    // HACK: These kind of things should not be done in the client. These services should all be microservices with their own
    // REST APIs. The client should do as little work / logic as possible
    getTotalBasketValue() {
        return this.getProductPackageBasketItems().then(basketItems => {
            var totalBasketValue = {
                currencySymbol: getSymbolFromCurrency(UserPersonalisationService.getValue(CURRENCY_CODE_KEY)),
                totalValueAsString: "",
                isDiscounted: false,
                discountedReason: "",
                undiscountedValueAsString: ""
            };
            var undiscountedValue = 0;
            var itemsCount = 0;

            basketItems.forEach(basketItem => {
                itemsCount += basketItem.count;
                undiscountedValue += basketItem.productPackage.totalLocalPrice * basketItem.count;
            });

            console.log(`Total basket items count: ${itemsCount}`);
            var discountedValue = undiscountedValue;

            if (itemsCount > 1) {
                console.log(`Discount is applicable`);
                totalBasketValue.isDiscounted = true;
                totalBasketValue.discountedReason = "10% off for buying multiple packages";
                discountedValue = 0.9 * undiscountedValue;
            }

            totalBasketValue.totalValueAsString = discountedValue.toFixed(2);
            totalBasketValue.undiscountedValueAsString = undiscountedValue.toFixed(2);

            return totalBasketValue;
        })
    }
}

export default new BasketService();