<template>
  <div class="container">
    <h3>All Product Packages</h3>
    <div v-if="message" class="alert alert-success">{{this.message}}</div>
    <div class="container">
      <table class="table">
        <thead>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Description</th>
            <th>Update</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="productPackage in productPackages" v-bind:key="productPackage.id">
            <td>{{productPackage.id}}</td>
            <td>{{productPackage.name}}</td>
            <td>{{productPackage.description}}</td>
            <td><button class="btn btn-success" v-on:click="updateProductPackage(productPackage.id)">Update</button></td>
            <td><button class="btn btn-warning" v-on:click="deleteProductPackage(productPackage.id)">Delete</button></td>
          </tr>
        </tbody>
        <div class="row">
          <button class="btn btn-success" v-on:click="addProductPackage()">Add</button>
        </div>
      </table>
    </div>
  </div>
</template>

<script>
import { NEW_PACKAGE_ROUTE, EDIT_PACKAGE_ROUTE } from '../routes';
import ProductPackageService from '../service/ProductPackageService';
export default {
  name: "ProductPackages",
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