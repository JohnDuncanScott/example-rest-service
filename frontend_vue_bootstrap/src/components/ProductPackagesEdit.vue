<template>
  <div class="container">
    <h3>Product Packages Admin</h3>
    <div v-if="message" class="alert alert-success">{{this.message}}</div>
    <div class="row my-2">
      <div class="col-1 float-start"><h6>Id</h6></div>
      <div class="col-2 float-start"><h6>Name</h6></div>
      <div class="col-4 float-start"><h6>Description</h6></div>
    </div>
    <div class="row my-2" v-for="productPackage in productPackages" v-bind:key="productPackage.id">
      <div class="col-1 float-start">{{productPackage.id}}</div>
      <div class="col-2 float-start">{{productPackage.name}}</div>
      <div class="col-4 float-start">{{productPackage.description}}</div>
      <div class="col-1 float-start mx-1"><button class="btn btn-success" v-on:click="updateProductPackage(productPackage.id)">Update</button></div>
      <div class="col-1 float-start mx-1"><button class="btn btn-warning" v-on:click="deleteProductPackage(productPackage.id)">Delete</button></div>
    </div>
    <div class="row">
      <div>
        <button class="btn btn-success" v-on:click="addProductPackage()">Add</button>
      </div>
    </div>
    </div>
</template>

<script>
import { NEW_PACKAGE_ROUTE, EDIT_PACKAGE_ROUTE } from '../routes';
import ProductPackageService from '../service/ProductPackageService';
export default {
  name: "ProductPackagesEdit",
  data() {
    return {
      productPackages: [],
      message: ""
    };
  },
  methods: {
    refreshProductPackages() {
      ProductPackageService.getAllProductPackages()
        .then(response => {
          if (response.data._embedded?.productPackageResourceList) {
            this.productPackages = response.data._embedded.productPackageResourceList;
          } else {
            this.productPackages = []
          }
        });
    },
    deleteProductPackage(id) {
      ProductPackageService.deleteProductPackage(id)
        .then(() => {
          this.message = `Deletion of product package ${id} was successful`;
          this.refreshProductPackages();
        });
    },
    updateProductPackage(id) {
      this.$router.push({ name: EDIT_PACKAGE_ROUTE, params: { id: id } });
    },
    addProductPackage() {
      this.$router.push({ name: NEW_PACKAGE_ROUTE });
    }
  },
  created() {
    this.refreshProductPackages();
  }
};
</script>

<style>
</style>