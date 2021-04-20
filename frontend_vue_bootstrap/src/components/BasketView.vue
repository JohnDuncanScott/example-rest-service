<template>
    <div class="container ms-2">
        <div class="row">
            <div class="div-section col-1 p-1 ps-2">Contents</div>
        </div>
        <div class="mt-2" v-if="this.productPackagesInBasket.length === 0">Nothing in basket</div>
        <div class="row align-items-center my-1 mt-2 div-basket-item" v-for="productPackageInBasket in productPackagesInBasket" v-bind:key="productPackageInBasket.productPackage.id">
            <div class="col-3 float-start me-2">{{productPackageInBasket.productPackage.name}}</div>
            <div class="col-3 float-start me-2">Individual price: {{getCurrencySymbol(productPackageInBasket.productPackage.localCurrency)}}{{productPackageInBasket.productPackage.totalLocalPrice}}</div>
            <div class="col-2 float-end">Quantity: {{productPackageInBasket.count}}</div>
            <div class="col-1 float-start"><button class="btn btn-add-to-cart float-end w-75" v-on:click="addToBasket(productPackageInBasket.productPackage.id)">+</button></div>
            <div class="col-1 float-start"><button class="btn btn-remove-from-cart float-start w-75" v-on:click="removeFromBasket(productPackageInBasket.productPackage.id)">-</button></div>
        </div>
        <div class="row mt-3">
            <div class="div-section col-1 p-1 ps-2">Total</div>
            <div class="mt-2">{{this.totalBasketValue.currencySymbol}}{{this.totalBasketValue.totalValueAsString}}</div>
            <div v-if="this.totalBasketValue.isDiscounted"><strike>{{this.totalBasketValue.currencySymbol}}{{this.totalBasketValue.undiscountedValueAsString}}</strike> ({{this.totalBasketValue.discountedReason}})</div>
        </div>
    </div>
</template>

<script>
import BasketService from '../service/BasketService';
import getSymbolFromCurrency from 'currency-symbol-map'
import { ADD_TO_BASKET_EVENT, REMOVE_FROM_BASKET_EVENT, CURRENCY_CHANGED_EVENT } from '../events'
export default {
    name: "BasketView",
    data() {
        return {
            totalBasketValue: {},
            productPackagesInBasket: []
        };
    },
    methods: {
        refreshProductPackages() {
            BasketService.getProductPackageBasketItems()
                .then(basketItems => {
                    this.productPackagesInBasket = basketItems;
                });
            BasketService.getTotalBasketValue()
                .then(totalBasketValue => {
                    this.totalBasketValue = totalBasketValue;
                })
        },
        addToBasket(id) {
            BasketService.addProductPackage(id);
            this.refreshProductPackages();
            this.$root.$emit(ADD_TO_BASKET_EVENT);
        },
        removeFromBasket(id) {
            BasketService.removeProductPackage(id);
            this.refreshProductPackages();
            this.$root.$emit(REMOVE_FROM_BASKET_EVENT);
        },
        getCurrencySymbol(currencyCode) {
            return getSymbolFromCurrency(currencyCode);
        }
    },
    created() {
        this.refreshProductPackages();
    },
    mounted() {
        this.$root.$on(CURRENCY_CHANGED_EVENT, () => {
            this.refreshProductPackages()
        });
    },
    beforeDestroy() {
        this.$root.$off(CURRENCY_CHANGED_EVENT)
    }
}
</script>
<style>
</style>