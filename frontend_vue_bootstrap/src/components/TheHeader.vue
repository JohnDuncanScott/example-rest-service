<template>
    <div class="container pb-2 pt-4">
      <header class="d-flex flex-wrap justify-content-between">
        <div class="d-flex">
          <div class="pe-1">
            <button type="button" class="btn btn-home" v-on:click="viewHome()">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="19"
                height="19"
                fill="currentColor"
                class="bi bi-house-fill"
                viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="m8 3.293 6 6V13.5a1.5 1.5 0 0 1-1.5 1.5h-9A1.5 1.5 0 0 1 2 13.5V9.293l6-6zm5-.793V6l-2-2V2.5a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5z"/>
                <path fill-rule="evenodd" d="M7.293 1.5a1 1 0 0 1 1.414 0l6.647 6.646a.5.5 0 0 1-.708.708L8 2.207 1.354 8.854a.5.5 0 1 1-.708-.708L7.293 1.5z"/>
              </svg>
            </button>
          </div>
          <div>
            <button type="button" class="btn btn-admin-warning" v-on:click="viewAdmin()">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="19"
                height="19"
                fill="currentColor"
                class="bi bi-gear"
                viewBox="0 0 16 16">
                <path d="M8 4.754a3.246 3.246 0 1 0 0 6.492 3.246 3.246 0 0 0 0-6.492zM5.754 8a2.246 2.246 0 1 1 4.492 0 2.246 2.246 0 0 1-4.492 0z"/>
                <path d="M9.796 1.343c-.527-1.79-3.065-1.79-3.592 0l-.094.319a.873.873 0 0 1-1.255.52l-.292-.16c-1.64-.892-3.433.902-2.54 2.541l.159.292a.873.873 0 0 1-.52 1.255l-.319.094c-1.79.527-1.79 3.065 0 3.592l.319.094a.873.873 0 0 1 .52 1.255l-.16.292c-.892 1.64.901 3.434 2.541 2.54l.292-.159a.873.873 0 0 1 1.255.52l.094.319c.527 1.79 3.065 1.79 3.592 0l.094-.319a.873.873 0 0 1 1.255-.52l.292.16c1.64.893 3.434-.902 2.54-2.541l-.159-.292a.873.873 0 0 1 .52-1.255l.319-.094c1.79-.527 1.79-3.065 0-3.592l-.319-.094a.873.873 0 0 1-.52-1.255l.16-.292c.893-1.64-.902-3.433-2.541-2.54l-.292.159a.873.873 0 0 1-1.255-.52l-.094-.319zm-2.633.283c.246-.835 1.428-.835 1.674 0l.094.319a1.873 1.873 0 0 0 2.693 1.115l.291-.16c.764-.415 1.6.42 1.184 1.185l-.159.292a1.873 1.873 0 0 0 1.116 2.692l.318.094c.835.246.835 1.428 0 1.674l-.319.094a1.873 1.873 0 0 0-1.115 2.693l.16.291c.415.764-.42 1.6-1.185 1.184l-.291-.159a1.873 1.873 0 0 0-2.693 1.116l-.094.318c-.246.835-1.428.835-1.674 0l-.094-.319a1.873 1.873 0 0 0-2.692-1.115l-.292.16c-.764.415-1.6-.42-1.184-1.185l.159-.291A1.873 1.873 0 0 0 1.945 8.93l-.319-.094c-.835-.246-.835-1.428 0-1.674l.319-.094A1.873 1.873 0 0 0 3.06 4.377l-.16-.292c-.415-.764.42-1.6 1.185-1.184l.292.159a1.873 1.873 0 0 0 2.692-1.115l.094-.319z"/>
              </svg>
            </button>
          </div>
        </div>
        <div class="d-flex text-center h2">Saruman's Store</div>
        <div class="d-flex">
          <div class="pe-1">
            <select class="form-select" aria-label="Currency selection" v-model="currencyCodeFromDropdown" @change="onCurrencyChange()">
              <option value="USD">USD</option>
              <option value="GBP">GBP</option>
              <option value="EUR">EUR</option>
            </select>
          </div>
          <div>
            <button type="button" class="btn btn-basket float-end" v-on:click="viewBasket()">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="18"
                height="18"
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
      </header>
    </div>
</template>

<script>
import { EDIT_PACKAGES_ROUTE, HOME_ROUTE, VIEW_BASKET_ROUTE } from '../routes';
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
    viewHome() {
      var currentRouteName = this.$route.name;

      if (currentRouteName !== HOME_ROUTE) {
        this.$router.push({ name: HOME_ROUTE });
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