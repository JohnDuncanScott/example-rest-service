<template>
  <div class="container">
    <div class="h3">Product Package Admin</div>
    <form @submit="validateAndSubmit">
      <div v-if="errors.length">
        <div class="alert alert-warning" v-bind:key="index" v-for="(error, index) in errors">{{error}}</div>
      </div>
      <fieldset class="form-group my-3">
        <label class="h6" v-if="id">ID</label>
        <input v-if="id" type="text" class="form-control" v-model="id" disabled>
      </fieldset>
      <fieldset class="form-group my-3">
        <label class="h6">Name</label>
        <input type="text" class="form-control" v-model="name">
      </fieldset>
      <fieldset class="form-group my-3">
        <label class="h6">Description</label>
        <input type="text" class="form-control" v-model="description">
      </fieldset>
      <fieldset class="form-group my-3">
        <label class="h6" v-if="id">Products</label>
        <textarea v-if="id" type="text" class="form-control" v-model="productsAsString" disabled></textarea>
      </fieldset>
      <button class="btn btn-admin-create mt-2" type="submit">Save</button>
    </form>
  </div>
</template>

<script>
import { EDIT_PACKAGES_ROUTE } from '../routes';
import ProductPackageService from '../service/ProductPackageService';

export default {
  name: "ProductPackageEdit",
  data() {
    return {
      name: "",
      description: "",
      products: [],
      productsAsString: "",
      errors: []
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
        if (!this.id) {
          return;
        }

        ProductPackageService.getProductPackage(this.id)
          .then(res => {
            this.name = res.data.name;
            this.description = res.data.description;
            this.products = res.data.products;
            this.productsAsString = JSON.stringify(this.products);
          });
    },
    validateAndSubmit(e) {
        e.preventDefault();
        this.errors = [];

        if (!this.name) {
          this.errors.push("Name cannot be empty");
        } else if (this.name.length < 5) {
          this.errors.push("Name must be at least 5 characters");
        }

        if (!this.description) {
          this.errors.push("Description cannot be empty");
        } else if (this.description.length < 5) {
          this.errors.push("Description must be at least 5 characters");
        }

        if(this.errors.length === 0) {
            if (this.id === null) {
                ProductPackageService.createProductPackage({
                    id: null,
                    name: this.name,
                    description: this.description
                  })
                  .then(() => {
                      this.$router.push({ name: EDIT_PACKAGES_ROUTE });
                  });
            } else {
                ProductPackageService.updateProductPackage(
                  this.id,
                  {
                    id: this.id,
                    name: this.name,
                    description: this.description,
                    products: this.products
                  })
                  .then(() => {
                      this.$router.push({ name: EDIT_PACKAGES_ROUTE });
                  });
            }
        }
    }
  }
};
</script>

<style>
</style>