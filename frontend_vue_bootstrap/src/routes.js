import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export const HOME_ROUTE = "Home";
export const PACKAGES_ROUTE = "Packages";
export const EDIT_PACKAGE_ROUTE = "EditPackage";
export const NEW_PACKAGE_ROUTE = "NewPackage";
export const VIEW_PACKAGE_ROUTE = "ViewPackage";

const router = new Router({
  mode: "history", // Use browser history
  routes: [
    {
      path: "/",
      name: HOME_ROUTE,
      component: () => import("./components/ProductPackagesView")
    },
    {
      path: "/admin/packages",
      name: PACKAGES_ROUTE,
      component: () => import("./components/ProductPackagesEdit")
    },
    {
      path: "/admin/packages/:id",
      name: EDIT_PACKAGE_ROUTE,
      component: () => import("./components/ProductPackageEdit")
    },
    {
      path: "/admin/packages/new",
      name: NEW_PACKAGE_ROUTE,
      component: () => import("./components/ProductPackageEdit")
    },
    {
      path: "/packages/:id",
      name: VIEW_PACKAGE_ROUTE,
      component: () => import("./components/ProductPackageView")
    }
  ]
});

export default router;