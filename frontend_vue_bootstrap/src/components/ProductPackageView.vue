<template>
  <div class="container">
    <h6>Name</h6>
    <div>{{this.productPackage.name}}</div>
    <h6 class="mt-3">Description</h6>
    <div>{{this.productPackage.description}}</div>
    <h6 class="mt-3">Contents</h6>
    <div v-for="product in productPackage.products" v-bind:key="product.id">
      <div>{{product.name}}</div>
    </div>
    <h6 class="mt-3">Price</h6>
    <div>{{getCurrencySymbol(this.productPackage.localCurrency)}}{{this.productPackage.totalLocalPrice}}</div>
    <div class="mt-3"><button class="btn btn-success" v-on:click="addToBasket()">Add to cart</button></div>
  </div>
</template>

<script>
import ProductPackageService from '../service/ProductPackageService';
import UserPersonalisationService from '../service/UserPersonalisationService';
import BasketService from '../service/BasketService';
import CURRENCY_CODE_KEY from '../service/UserPersonalisationService';
import getSymbolFromCurrency from 'currency-symbol-map'
import { ADD_TO_BASKET_EVENT, CURRENCY_CHANGED_EVENT } from '../events'
export default {
  name: "ProductPackageView",
  data() {
    return {
      currencyCode: "",
      productPackage: {}
    };
  },
  computed: {
    id() {
      return this.$route.params.id;
    }
  },
  created() {
    this.currencyCode = UserPersonalisationService.getValue(CURRENCY_CODE_KEY);
    this.refreshProductPackage();
  },
  mounted() {
    this.$root.$on(CURRENCY_CHANGED_EVENT, (currencyCodeFromDropdown) => {
      this.currencyCode = currencyCodeFromDropdown
      this.refreshProductPackage()
    });
  },
  beforeDestroy() {
    this.$root.$off(CURRENCY_CHANGED_EVENT)
  },
  methods: {
    refreshProductPackage() {
        ProductPackageService.getProductPackage(this.id, this.currencyCode)
          .then(res => {
            this.productPackage = res.data;
          });
    },
    addToBasket() {
      console.log(`Adding package to basket, id: ${this.productPackage.id}`);
      BasketService.addProductPackage(this.productPackage.id);
      this.$root.$emit(ADD_TO_BASKET_EVENT);
    },
    getCurrencySymbol(currencyCode) {
      return getSymbolFromCurrency(currencyCode);
    }
  }
};
</script>

<style>
</style>