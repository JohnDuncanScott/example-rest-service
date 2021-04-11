import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const router = new Router({
  mode: "history", // Use browser history
  routes: [
    {
      path: "/",
      name: "Home",
      component: () => import("./components/ProductPackages")
    },
    {
      path: "/packages",
      name: "Product packages",
      component: () => import("./components/ProductPackages")
    },
    {
      path: "/packages/:id",
      name: "Product package details",
      component: () => import("./components/ProductPackage")
    }
  ]
});

export default router;