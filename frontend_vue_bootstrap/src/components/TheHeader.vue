<template>
    <div class="container-fluid">
      <div class="row">
        <div class="col">Welcome to the shop!</div>
        <div class="col-1">
          Currency:
        </div>
        <div class="col-2">
          <keep-alive>
            <select class="form-select" aria-label="Currency selection" v-model="currencyCodeFromDropdown" @change="onCurrencyChange()">
              <option value="USD">USD</option>
              <option value="GBP">GBP</option>
              <option value="EUR">EUR</option>
            </select>
          </keep-alive>
        </div>
        <div class="col-1">
          <button type="button" class="btn btn-primary" v-on:click="viewBasket()">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              fill="currentColor"
              class="bi bi-cart"
              viewBox="0 0 16 16">
                <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l1.313 7h8.17l1.313-7H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
            </svg>
            <div>{{this.currencySymbol}} 5,542</div>
          </button>
        </div>
      </div>
    </div>
</template>

<script>
import { VIEW_BASKET_ROUTE } from '../routes';
import getSymbolFromCurrency from 'currency-symbol-map';
import { CURRENCY_CHANGED_EVENT } from '../events';
import UserPersonalisationService from '../service/UserPersonalisationService';
import CURRENCY_CODE_KEY from '../service/UserPersonalisationService';
export default {
  name: "TheHeader",
  data() {
    return {
      currencyCodeFromDropdown: ""
    };
  },
  computed: {
    currencySymbol() {
      return getSymbolFromCurrency(this.currencyCodeFromDropdown);
    }
  },
  methods: {
    onCurrencyChange() {
      console.log(`Currency changed: ${this.currencyCodeFromDropdown}`)
      console.log(CURRENCY_CHANGED_EVENT)
      UserPersonalisationService.storeValue(CURRENCY_CODE_KEY, this.currencyCodeFromDropdown);
      this.$root.$emit(CURRENCY_CHANGED_EVENT, this.currencyCodeFromDropdown)
    },
    viewBasket() {
      this.$router.push({ name: VIEW_BASKET_ROUTE });
    },
  },
  created() {
      var currencyCode = UserPersonalisationService.getValue(CURRENCY_CODE_KEY);

      if (!currencyCode) {
        // Match defaults for API. You would probably do this based on location in a real product
        currencyCode = "USD";
        UserPersonalisationService.storeValue(CURRENCY_CODE_KEY, currencyCode);
      }

      this.currencyCodeFromDropdown = currencyCode;
  }
};
</script>

<style>
</style>