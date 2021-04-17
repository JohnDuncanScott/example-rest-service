<template>
  <div class="container">
      <TheHeader/>
    <router-view/>
  </div>
</template>

<script>
import TheHeader from './components/TheHeader.vue';
export default {
  components: { TheHeader },
  name: "app"
};
</script>

<style>
/* @import url(https://unpkg.com/bootstrap@4.1.0/dist/css/bootstrap.min.css) */
@import url(https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css)
</style>
