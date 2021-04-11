<template>
  <div class="container">
    <TheHeader/>
    <h3>Name</h3>
    <div>{{this.name}}</div>
    <h3>Description</h3>
    <div>{{this.description}}</div>
  </div>
</template>

<script>
import ProductPackageService from '../service/ProductPackageService';
import TheHeader from './TheHeader.vue';
export default {
  components: { TheHeader },
  name: "ProductPackageView",
  data() {
    return {
      name: "",
      description: ""
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
  methods: {
    refreshProductPackage() {
        ProductPackageService.getProductPackage(this.id)
          .then(res => {
            this.name = res.data.name;
            this.description = res.data.description;
          });
    }
  }
};
</script>

<style>
</style>