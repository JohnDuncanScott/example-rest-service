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
            <td><button class="btn btn-success" v-on:click="addToBasket(productPackage)">Add to cart</button></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { VIEW_PACKAGE_ROUTE } from '../routes';
import ProductPackageService from '../service/ProductPackageService';
import BasketService from '../service/BasketService';
import UserPersonalisationService from '../service/UserPersonalisationService';
import CURRENCY_CODE_KEY from '../service/UserPersonalisationService';
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
    addToBasket(productPackage) {
      console.log(`Adding package to basket, id: ${productPackage.id}`)
      BasketService.addProductPackage(productPackage)
    },
    getCurrencySymbol(currencyCode) {
      return getSymbolFromCurrency(currencyCode);
    }
  },
  created() {
    this.currencyCode = UserPersonalisationService.getValue(CURRENCY_CODE_KEY);
    this.refreshProductPackages();
  },
  mounted() {
    this.$root.$on(CURRENCY_CHANGED_EVENT, (currencyCodeFromDropdown) => {
      this.currencyCode = currencyCodeFromDropdown
      this.refreshProductPackages()
    });
  },
  beforeDestroy() {
    this.$root.$off(CURRENCY_CHANGED_EVENT)
  }
};
</script>

<style>
</style>