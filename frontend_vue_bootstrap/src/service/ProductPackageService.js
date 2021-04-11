import axios from "axios";

const PRODUCT_PACKAGE_API_URL = "http://localhost:8080/packages";

class ProductPackageService {
    getAllProductPackages() {
        console.log(`Getting all product packages`)
        return axios.get(`${PRODUCT_PACKAGE_API_URL}`);
    }

    getProductPackage(id) {
        console.log(`Getting product package with id ${id}`);
        return axios.get(`${PRODUCT_PACKAGE_API_URL}/${id}`);
    }

    deleteProductPackage(id) {
        console.log(`Deleting product package with id ${id}`);
        return axios.delete(`${PRODUCT_PACKAGE_API_URL}/${id}`);
    }

    updateProductPackage(id, productPackage) {
        console.log(`Updating product package with id ${id}, ${productPackage}`);
        return axios.put(`${PRODUCT_PACKAGE_API_URL}/${id}`, productPackage);
    }
  
    createProductPackage(productPackage) {
        console.log(`Creating product package: ${productPackage}`);
        return axios.post(`${PRODUCT_PACKAGE_API_URL}`, productPackage);
    }
}

export default new ProductPackageService();