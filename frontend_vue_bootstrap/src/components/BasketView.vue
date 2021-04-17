<template>
    <div class=container>
        <div v-if="Object.keys(productPackagesInBasket).length === 0">
            Nothing in basket currently
        </div>
        <table class="table">
            <tbody>
            <tr v-for="productPackageInBasket in productPackagesInBasket" v-bind:key="productPackageInBasket.productPackage.id">
                <td>{{productPackageInBasket.productPackage.name}}</td>
                <td>Quantity: {{productPackageInBasket.count}}</td>
                <td><button class="btn btn-success" v-on:click="addToBasket(productPackageInBasket.productPackage)">+</button></td>
                <td><button class="btn btn-danger" v-on:click="removeFromBasket(productPackageInBasket.productPackage)">-</button></td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import BasketService from '../service/BasketService';
export default {
    name: "BasketView",
    data() {
        return {
            productPackagesInBasket: {}
        };
    },
    methods: {
        refreshProductPackages() {
            this.productPackagesInBasket = BasketService.getAllProductPackages();
        },
        addToBasket(productPackage) {
            BasketService.addProductPackage(productPackage);
            this.refreshProductPackages();
        },
        removeFromBasket(productPackage) {
            BasketService.removeProductPackage(productPackage);
            this.refreshProductPackages();
        }
    },
    created() {
        this.refreshProductPackages();
    }
}
</script>
<style>
</style>