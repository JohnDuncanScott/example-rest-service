<template>
    <div class="container">
      <div class="row">
        <div class="col">Welcome to the shop!</div>
        <div class="col-1 float-end">
          <button type="button" class="btn btn-danger" v-on:click="viewAdmin()">Admin</button>
        </div>
        <div class="col-1 float-end">
          Currency:
        </div>
        <div class="col-2 float-end">
          <select class="form-select" aria-label="Currency selection" v-model="currencyCodeFromDropdown" @change="onCurrencyChange()">
            <option value="USD">USD</option>
            <option value="GBP">GBP</option>
            <option value="EUR">EUR</option>
          </select>
        </div>
        <div class="col-3 float-end">
          <button type="button" class="btn btn-primary float-end" v-on:click="viewBasket()">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              fill="currentColor"
              class="bi bi-cart float-start mx-2"
              viewBox="0 0 16 16">
                <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l1.313 7h8.17l1.313-7H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
            </svg>
            <div class="float-start mx-2">{{this.totalBasketValue.currencySymbol}}{{this.totalBasketValue.totalValueAsString}}</div>
            <div class="float-start mx-2" v-if="this.totalBasketValue.isDiscounted"><strike>{{this.totalBasketValue.currencySymbol}}{{this.totalBasketValue.undiscountedValueAsString}}</strike></div>
          </button>
        </div>
      </div>
    </div>
</template>

<script>
import { EDIT_PACKAGES_ROUTE, VIEW_BASKET_ROUTE } from '../routes';
import getSymbolFromCurrency from 'currency-symbol-map';
import { ADD_TO_BASKET_EVENT, REMOVE_FROM_BASKET_EVENT, CURRENCY_CHANGED_EVENT } from '../events';
import UserPersonalisationService from '../service/UserPersonalisationService';
import CURRENCY_CODE_KEY from '../service/UserPersonalisationService';
import BasketService from '../service/BasketService';
export default {
  name: "TheHeader",
  data() {
    return {
      currencyCodeFromDropdown: "",
      totalBasketValue: {}
    };
  },
  computed: {
    currencySymbol() {
      return getSymbolFromCurrency(this.currencyCodeFromDropdown);
    }
  },
  methods: {
    onCurrencyChange() {
      console.log(`Currency changed: ${this.currencyCodeFromDropdown}`);
      console.log(CURRENCY_CHANGED_EVENT);
      UserPersonalisationService.storeValue(CURRENCY_CODE_KEY, this.currencyCodeFromDropdown);
      this.refresh();
      this.$root.$emit(CURRENCY_CHANGED_EVENT, this.currencyCodeFromDropdown);
    },
    viewAdmin() {
      var currentRouteName = this.$route.name;

      if (currentRouteName !== EDIT_PACKAGES_ROUTE) {
        this.$router.push({ name: EDIT_PACKAGES_ROUTE });
      }
    },
    viewBasket() {
      var currentRouteName = this.$route.name;

      if (currentRouteName !== VIEW_BASKET_ROUTE) {
        this.$router.push({ name: VIEW_BASKET_ROUTE });
      }
    },
    updateBasketTotal() {
      BasketService.getTotalBasketValue()
        .then(totalBasketValue => {
          this.totalBasketValue = totalBasketValue;
        })
    },
    refresh() {
      this.updateBasketTotal();
    }
  },
  created() {
      var currencyCode = UserPersonalisationService.getValue(CURRENCY_CODE_KEY);

      if (!currencyCode) {
        // Match defaults for API. You would probably do this based on location in a real product
        currencyCode = "USD";
        UserPersonalisationService.storeValue(CURRENCY_CODE_KEY, currencyCode);
      }

      this.currencyCodeFromDropdown = currencyCode;

      this.refresh();
  },
  mounted() {
    this.$root.$on(ADD_TO_BASKET_EVENT, this.refresh);
    this.$root.$on(REMOVE_FROM_BASKET_EVENT, this.refresh);
  },
  beforeDestroy() {
    this.$root.$off(ADD_TO_BASKET_EVENT);
    this.$root.$off(REMOVE_FROM_BASKET_EVENT);
  }
};
</script>

<style>
</style>