# ExampleRestService
This project showcases a simple REST service.
I have not done significant web development work, so this is mainly
a learning project for me to look at the many options and pick something
easy to start with.

# Tech rationales
* Build system
  * **Maven** - Mature build system, can find a lot of tutorials for it,
  have used it for personal projects
* Backend service
  * **Java** - still popular and versatile. For C#, Blazor looks
  like an interesting competitor, but doesn't use JavaScript, and want to use
  JavaScript
  * <https://rollbar.com/blog/spring-boot-vs-spring-mvc-differences-and-when-to-use-each/>
    * Go with **Spring Boot** for speed
* Frontend framework
  * <https://www.keycdn.com/blog/frontend-frameworks>
  * **React** is hard to pickup due to inconsistent project docs
  * **Vue** has excellent docs and tutorials, and is easy to learn
  * **Angular** has a steep learning curve
  * Conclusion: **Vue** should be best for quick prototyping due to its
  learning curve and its popularity
* Styling
  * **Bootstrap** has good docs and is robust, plus it's more versatile
  * **Vuetify** follows Material Design, so doesn't fit all applications
  * **Quasar** has high complexity but has a lot of inbuilt best practices,
  also follows Material Design. Built by 1 person
  * Useful links:
    * <https://www.reddit.com/r/vuejs/comments/arr8kt/vuetify_vs_bootstrap/>
    * <https://blog.bitsrc.io/quasar-vs-vutify-vs-bootstrap-vue-choosing-the-right-vuejs-ui-library-cf566f61bc4>
    * <https://medium.com/pilcro/should-you-use-material-design-bfb596a04bae>
    * <https://medium.com/the-web-tub/choosing-the-right-front-end-framework-for-your-vue-app-4448bac12ce7>
    * **Bootstrap** specific:
      * (How does Bootstrap 5 Spacing work?)[https://www.youtube.com/watch?v=7IBbewitYxA]
   * Conclusion: For quick prototyping and flexibility, **Bootstrap** seems best choice
* Frontend
  * As a result of picking **Vue** and **Bootstrap**, there are more options:
    * **Bootstrap-Vue** (combines them, removing jQuery)
      * <https://bootstrap-vue.org/>
      * Good docs, good for beginners, flexible, popular
      * However, don't have experience with either, so combining both at once will probably
      be high mental load
    * **Vue** + **Bootstrap v5** (v5 has jQuery removed, so 2 work together nicely now)
      * <https://dev.to/codeply/using-bootstrap-5-with-vue-js-5fnp>
      * <https://dev.to/tefoh/use-bootstrap-5-in-vue-correctly-2k2g>
      * <https://stackoverflow.com/questions/65547199/using-bootstrap-5-with-vue-3>
      * **Note:** Bootstrap v5 is in Beta. Normally wouldn't use this for prod
      but for prototyping this is ok
    * Conclusion: Go with plain **Vue** + **Bootstrap v5** for styling. As I'm new to
    both frameworks, working with them independently first is a better intro. Migrating
    to Bootstrap-Vue later (if even needed) should be reasonably simple
* Summary:
  * Build system: **Maven**
  * Backend: **Java** + **Spring Boot**
  * Frontend: **Vue** + **Bootstrap v5**
  * Tutorial: <https://www.springboottutorial.com/spring-boot-vue-full-stack-crud-maven-application>
    * Errata
      * The GitHub repo [here](https://github.com/in28minutes/spring-boot-vuejs-fullstack-examples/tree/master/spring-boot-crud-full-stack) seems mostly correct (but also has a few mistakes, like not updating the frontend service port), but if you're following the instructions then the below points should help (also one of the first commits in my repo has this working fully)
      * "Implementing Routing" section - 3 components should be:
        * `./components/ListCoursesComponent`
        * `./components/ListCoursesComponent`
        * `./components/CourseComponent`
      * "Adding Add button to Course Listing Page" section:
        * `this.$router.push(``/course/-1``);` should be `this.$router.push(``/courses/-1``);` (should be plural)
      * "Adding Validation using Formik" section:
        * `validate(values)` should be `validateAndSubmit(e)`
        * `<div class="alert alert-warning" v-bind:key="index" v-for="(error, index) in errors"></div>` should be `<div class="alert alert-warning" v-bind:key="index" v-for="(error, index) in errors">{{error}}</div>` (the `{{error}}` is missing)

# Tooling
* Backend - IntelliJ Community Edition
* Frontend - Visual Studio Code with:
  * Markdown All in One
  * Markdown Preview Enhanced
  * Vetur (Vue plugin)
* Browser
  * <https://github.com/vuejs/vue-devtools> (doesn't work in Edge, Chrome is fine)

# Instructions
* Requirements
  * Install [Java Development Kit 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) for your platform
  * Install [nodejs](https://nodejs.org/en/) for your platform
  
* Frontend
  * cd frontend_vue_bootstrap
  * npm install
  * npm run serve