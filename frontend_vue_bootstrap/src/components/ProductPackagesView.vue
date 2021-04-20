<template>
  <div class="container">
    <div class="col-4 my-1">
      <input type="text" class="form-control" placeholder="Search" aria-label="Search" aria-describedby="button-search" v-model="search">
    </div>
    <div class="col-4 mt-1 mb-4">
      <select class="form-select" aria-label="Filter selection" v-model="filterSelection">
        <option v-bind:value="{ key: 'name', isAsc: true }">Sort by: Name (A-Z)</option>
        <option v-bind:value="{ key: 'name', isAsc: false }">Sort by: Name (Z-A)</option>
        <option v-bind:value="{ key: 'totalLocalPrice', isAsc: true }">Sort by: Price (Lowest to Highest)</option>
        <option v-bind:value="{ key: 'totalLocalPrice', isAsc: false }">Sort by: Price (Highest to Lowest)</option>
      </select>
    </div>
    <div class="row my-2 ms-1" v-if="this.productPackages.length === 0">Loading...</div>
    <div class="row my-2" v-for="productPackage in filteredProductPackages" v-bind:key="productPackage.id">
      <div class="col-4 float-start">
        <div class="btn btn-shop-item" v-on:click="viewProductPackage(productPackage.id)">
          <div class="float-start me-4">{{productPackage.name}}</div>
          <div class="float-end">{{getCurrencySymbol(productPackage.localCurrency)}}{{productPackage.totalLocalPrice}}</div>
        </div>
      </div>
      <div class="col-2 float-start"><button class="btn btn-add-to-cart" v-on:click="addToBasket(productPackage.id)">Add to cart</button></div>
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
import { ADD_TO_BASKET_EVENT, CURRENCY_CHANGED_EVENT } from '../events'

export default {
  name: "ProductPackagesView",
  data() {
    return {
      search: "",
      currencyCode: "",
      filterSelection: { key: 'name', isAsc: true },
      productPackages: []
    };
  },
  computed: {
    filteredProductPackages() {
      console.log(`Search: ${this.search}`);
      console.log(`Sort by key: ${this.filterSelection.key}`);
      console.log(`Sort by ascending: ${this.filterSelection.isAsc}`);
      var filteredProductPackages = this.productPackages;

      if (this.search) {
        filteredProductPackages = filteredProductPackages
          .filter(p => p.name.toLowerCase().includes(this.search.toLowerCase()));
      }

      filteredProductPackages.sort((p1, p2) => {
            var value1 = p1[this.filterSelection.key];
            var value2 = p2[this.filterSelection.key];

            if (value1 < value2) {
              return -1;
            }
            
            if (value1 > value2){
              return 1;
            }

            return 0;
        });

        if (!this.filterSelection.isAsc) {
          filteredProductPackages.reverse();
        }

        return filteredProductPackages;
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
    addToBasket(id) {
      console.log(`Adding package to basket, id: ${id}`);
      BasketService.addProductPackage(id);
      this.$root.$emit(ADD_TO_BASKET_EVENT);
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