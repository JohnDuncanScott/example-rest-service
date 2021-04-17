<template>
  <div class="container">
    <h3>Name</h3>
    <div>{{this.name}}</div>
    <h3>Description</h3>
    <div>{{this.description}}</div>
    <h3>Price</h3>
    <div>{{getCurrencySymbol(this.localCurrency)}}{{this.totalLocalPrice}}</div>
  </div>
</template>

<script>
import ProductPackageService from '../service/ProductPackageService';
import getSymbolFromCurrency from 'currency-symbol-map'
import { CURRENCY_CHANGED_EVENT } from '../events'
export default {
  name: "ProductPackageView",
  data() {
    return {
      currencyCode: "",
      name: "",
      description: "",
      totalLocalPrice: 0
    };
  },
  computed: {
    id() {
      var id = this.$route.params.id;

      if (id === "new") {
        id = null;
      }

      return id;
    }
  },
  created() {
    this.refreshProductPackage();
  },
  mounted() {
    this.$root.$on(CURRENCY_CHANGED_EVENT, (currencyCode) => {
      this.currencyCode = currencyCode
      this.refreshProductPackage()
    });
  },
  methods: {
    refreshProductPackage() {
        ProductPackageService.getProductPackage(this.id, this.currencyCode)
          .then(res => {
            this.name = res.data.name;
            this.description = res.data.description;
            this.localCurrency = res.data.localCurrency;
            this.totalLocalPrice = res.data.totalLocalPrice;
          });
    },
    getCurrencySymbol(currencyCode) {
      return getSymbolFromCurrency(currencyCode);
    }
  }
};
</script>

<style>
</style>