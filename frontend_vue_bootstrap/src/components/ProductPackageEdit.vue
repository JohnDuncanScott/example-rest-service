<template>
  <div>
    <h3>Product Package Admin</h3>
    <div class="container">
      <form @submit="validateAndSubmit">
        <div v-if="errors.length">
          <div class="alert alert-warning" v-bind:key="index" v-for="(error, index) in errors">{{error}}</div>
        </div>
        <fieldset class="form-group">
          <label v-if="id">Id</label>
          <input v-if="id" type="text" class="form-control" v-model="id" disabled>
        </fieldset>
        <fieldset class="form-group">
          <label>Name</label>
          <input type="text" class="form-control" v-model="name">
        </fieldset>
        <fieldset class="form-group">
          <label>Description</label>
          <input type="text" class="form-control" v-model="description">
        </fieldset>
        <button class="btn btn-success" type="submit">Save</button>
      </form>
    </div>
  </div>
</template>

<script>
import { PACKAGES_ROUTE } from '../routes';
import ProductPackageService from '../service/ProductPackageService';

export default {
  name: "ProductPackageEdit",
  data() {
    return {
      name: "",
      description: "",
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
        ProductPackageService.getProductPackage(this.id)
          .then(res => {
            this.name = res.data.name;
            this.description = res.data.description;
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
                      this.$router.push({ name: PACKAGES_ROUTE });
                  });
            } else {
                ProductPackageService.updateProductPackage(
                  this.id,
                  {
                    id: this.id,
                    name: this.name,
                    description: this.description
                  })
                  .then(() => {
                      this.$router.push({ name: PACKAGES_ROUTE });
                  });
            }
        }
    }
  }
};
</script>

<style>
</style>