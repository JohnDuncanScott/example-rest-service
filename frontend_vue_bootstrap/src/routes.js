import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const router = new Router({
  mode: "history", // Use browser history
  routes: [
    {
      path: "/",
      name: "Home",
      component: () => import("./components/ListCoursesComponent")
    },
    {
      path: "/courses",
      name: "Courses",
      component: () => import("./components/ListCoursesComponent")
    },
    {
      path: "/courses/:id",
      name: "Course Details",
      component: () => import("./components/CourseComponent")
    }
  ]
});

export default router;