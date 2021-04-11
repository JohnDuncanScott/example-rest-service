import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export const HOME_ROUTE = "Home";
export const PACKAGES_ROUTE = "Packages";
export const EDIT_PACKAGE_ROUTE = "EditPackage";
export const NEW_PACKAGE_ROUTE = "NewPackage";

const router = new Router({
  mode: "history", // Use browser history
  routes: [
    {
      path: "/",
      name: HOME_ROUTE,
      component: () => import("./components/EditProductPackages")
    },
    {
      path: "/admin/packages",
      name: PACKAGES_ROUTE,
      component: () => import("./components/EditProductPackages")
    },
    {
      path: "/admin/packages/:id",
      name: EDIT_PACKAGE_ROUTE,
      component: () => import("./components/EditProductPackage")
    },
    {
      path: "/admin/packages/new",
      name: NEW_PACKAGE_ROUTE,
      component: () => import("./components/EditProductPackage")
    }
  ]
});

export default router;