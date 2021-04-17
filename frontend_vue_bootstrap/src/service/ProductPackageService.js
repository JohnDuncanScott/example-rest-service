import axios from "axios";

const BASE_URL = new URL("http://localhost:8080");
const PACKAGES_PATH = "packages"

class ProductPackageService {
    getAllProductPackages(currencyCode) {
        console.log(`Getting all product packages with currency code: ${currencyCode}`)
        var url = new URL(`${PACKAGES_PATH}`, BASE_URL)
        this.appendCurrencyCode(url, currencyCode)
        console.log(url)
        return axios.get(url);
    }

    getProductPackage(id, currencyCode) {
        console.log(`Getting product package with id: ${id}, currency: ${currencyCode}`);
        var url = this.buildPackagesPathWithId(id)
        this.appendCurrencyCode(url, currencyCode)
        console.log(url)
        return axios.get(url);
    }

    deleteProductPackage(id) {
        console.log(`Deleting product package with id ${id}`);
        var url = this.buildPackagesPathWithId(id)
        console.log(url)
        return axios.delete(url);
    }

    updateProductPackage(id, productPackage) {
        console.log(`Updating product package with id ${id}, ${productPackage}`);
        var url = this.buildPackagesPathWithId(id)
        console.log(url)
        return axios.put(url, productPackage);
    }
  
    createProductPackage(productPackage) {
        console.log(`Creating product package: ${productPackage}`);
        var url = this.buildPackagesPath()
        console.log(url)
        return axios.post(url, productPackage);
    }

    buildPackagesPath() {
        return new URL(`${PACKAGES_PATH}`, BASE_URL)
    }

    buildPackagesPathWithId(id) {
        return new URL(`${PACKAGES_PATH}/${id}`, BASE_URL)
    }

    appendCurrencyCode(url, currencyCode) {
        if (currencyCode) {
            url.searchParams.set("cs", currencyCode)
        }
    }
}

export default new ProductPackageService();