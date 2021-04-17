<template>
  <div class="container">
    <div class="input-group mb-3">
      <input type="text" class="form-control" placeholder="Search" aria-label="Search" aria-describedby="button-search" v-model="search">
    </div>
    <div class="container">
      <table class="table">
        <tbody>
          <tr v-for="productPackage in filteredProductPackages" v-bind:key="productPackage.id">
            <td>{{productPackage.name}}</td>
            <td>{{getCurrencySymbol(productPackage.localCurrency)}}{{productPackage.totalLocalPrice}}</td>
            <td><button class="btn btn-info" v-on:click="viewProductPackage(productPackage.id)">Details</button></td>
            <td><button class="btn btn-success">Add to cart</button></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { VIEW_PACKAGE_ROUTE } from '../routes';
import ProductPackageService from '../service/ProductPackageService';
import getSymbolFromCurrency from 'currency-symbol-map'
import { CURRENCY_CHANGED_EVENT } from '../events'
export default {
  name: "ProductPackagesView",
  data() {
    return {
      search: "",
      currencyCode: "",
      productPackages: []
    };
  },
  computed: {
    filteredProductPackages() {
      if (!this.search) {
        return this.productPackages;
      }

      return this.productPackages.filter(p => p.name.toLowerCase().includes(this.search.toLowerCase()));
    }
  },
  methods: {
    refreshProductPackages() {
      ProductPackageService.getAllProductPackages(this.currencyCode)
        .then(response => {
          if (response.data._embedded?.productPackageResourceList) {
            this.productPackages = response.data._embedded.productPackageResourceList;
          } else {
            this.productPackages = []
          }
        });
    },
    viewProductPackage(id) {
      this.$router.push({ name: VIEW_PACKAGE_ROUTE, params: { id: id } });
    },
    getCurrencySymbol(currencyCode) {
      return getSymbolFromCurrency(currencyCode);
    }
  },
  created() {
    this.refreshProductPackages();
  },
  mounted() {
    this.$root.$on(CURRENCY_CHANGED_EVENT, (currencyCode) => {
      this.currencyCode = currencyCode
      this.refreshProductPackages()
    });
  }
};
</script>

<style>
</style>