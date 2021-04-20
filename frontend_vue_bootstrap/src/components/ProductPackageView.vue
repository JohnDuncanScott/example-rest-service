<template>
  <div class="container">
    <div class="div-section col-2 p-1 ps-2 mb-1">Name</div>
    <div>{{this.productPackage.name}}</div>
    <div class="div-section col-2 mt-4 mb-1 p-1 ps-2">Description</div>
    <div>{{this.productPackage.description}}</div>
    <div class="div-section col-2 mt-4 p-1 ps-2 mb-1">Contents</div>
    <div v-for="product in productPackage.products" v-bind:key="product.id">
      <!-- HACK: Can't have multiple products currently, so just hardcode -->
      <div>{{product.name}} x 1</div>
    </div>
    <div class="div-section col-2 mt-4 p-1 ps-2 mb-1">Price</div>
    <div>{{getCurrencySymbol(this.productPackage.localCurrency)}}{{this.productPackage.totalLocalPrice}}</div>
    <div class="mt-4"><button class="btn btn-add-to-cart" v-on:click="addToBasket()">Add to cart</button></div>
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