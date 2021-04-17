import Vue from 'vue'

// TODO: This does nothing but store data. Ideally this would actually be storing basket information
// for the user via another REST service
class BasketService {
    // HACK: Keep state
    static productPackagesInBasket = {}

    addProductPackage(productPackage) {
        var productPackageInBasket = BasketService.productPackagesInBasket[productPackage.id];

        if (productPackageInBasket) {
            console.log(`Package already in basket, incrementing count by 1, id: ${productPackage.id}`);
            BasketService.productPackagesInBasket[productPackage.id].count = productPackageInBasket.count + 1;
            console.log(`Package count: ${BasketService.productPackagesInBasket[productPackage.id].count}`);
        } else {
            console.log(`Added package to basket for first time in basket, id: ${productPackage.id}`);
            var newProductPackageInBasket = { productPackage: productPackage }
            // Makes sure count updating causes reactions
            Vue.set(newProductPackageInBasket, 'count', 1)
            BasketService.productPackagesInBasket[productPackage.id] = newProductPackageInBasket;
        }
    }

    // TODO: No need for whole package, just need id
    removeProductPackage(productPackage) {
        var productPackageInBasket = BasketService.productPackagesInBasket[productPackage.id];

        if (productPackageInBasket) {
            console.log(`Removing package from basket, id: ${productPackage.id}`);

            if (productPackageInBasket.count > 1) {
                console.log(`More than 1 in basket, decrementing count by 1, id: ${productPackage.id}`);
                BasketService.productPackagesInBasket[productPackage.id].count = productPackageInBasket.count - 1;
            } else {
                // Without this, the basket won't actually remove the final item. See:
                // https://stackoverflow.com/questions/50782129/deleting-an-object-by-key-doesnt-update-the-vue-component
                Vue.delete(BasketService.productPackagesInBasket, productPackage.id);
            }
        } else {
            console.log(`Could not remove package from basket, couldn't find, id: ${productPackage.id}`);
        }
    }

    getAllProductPackages() {
        return BasketService.productPackagesInBasket;
    }
}

export default new BasketService();