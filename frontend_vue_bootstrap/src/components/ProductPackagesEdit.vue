<template>
  <div class="container">
    <div class="h3 mb-4">Product Packages Admin</div>
    <div v-if="message" class="alert alert-success">{{this.message}}</div>
    <div class="row mb-1 ms-0">
      <div class="col-1 float-start h6">ID</div>
      <div class="col-2 float-start h6">Name</div>
      <div class="col-4 float-start h6">Description</div>
    </div>
    <div class="row mb-2 div-section align-items-center ms-0" v-for="productPackage in productPackages" v-bind:key="productPackage.id">
      <div class="col-1 float-start">{{productPackage.id}}</div>
      <div class="col-2 float-start">{{productPackage.name}}</div>
      <div class="col-4 float-start">{{productPackage.description}}</div>
      <div class="col-1 float-start mx-1"><button class="btn btn-admin-modify" v-on:click="updateProductPackage(productPackage.id)">Update</button></div>
      <div class="col-1 float-start mx-1"><button class="btn btn-admin-warning" v-on:click="deleteProductPackage(productPackage.id)">Delete</button></div>
    </div>
    <div class="row">
      <div class="col-1">
        <button class="btn btn-admin-create" v-on:click="addProductPackage()">Add</button>
      </div>
    </div>
    </div>
</template>

<script>
import { NEW_PACKAGE_ROUTE, EDIT_PACKAGE_ROUTE } from '../routes';
import ProductPackageService from '../service/ProductPackageService';
import BasketService from '../service/BasketService';
import { REMOVE_FROM_BASKET_EVENT } from '../events'
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
        })
        .then();

      BasketService.deleteProductPackage(id);
      this.$root.$emit(REMOVE_FROM_BASKET_EVENT);
    },
    updateProductPackage(id) {
      this.$router.push({ name: EDIT_PACKAGE_ROUTE, params: { id: id } });
    },
    addProductPackage() {
      this.$router.push({ name: NEW_PACKAGE_ROUTE, params: { id: "new" }  });
    }
  },
  created() {
    this.refreshProductPackages();
  }
};
</script>

<style>
</style>