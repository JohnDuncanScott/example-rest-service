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

# Tips
* REST
  * [Complex searches](https://softwareengineering.stackexchange.com/questions/353086/what-is-a-proper-way-to-do-a-complex-restful-search-method)
  * Assembling complex objects
    * [Best example using HATEOS](https://softwareengineering.stackexchange.com/questions/327804/rest-api-handle-partial-nested-objects)
    * [Performance-based discussion](http://blog.shippable.com/rest-api-best-practice-assemble-complex-objects-in-the-ui-layer)
  * Spring HATEOS - making model representation easy
    * [Official docs](https://docs.spring.io/spring-hateoas/docs/current/reference/html/#reference)
    * [Baeldung tutorial](https://www.baeldung.com/spring-hateoas-tutorial)
    * [Comparison of REST and HATEOS](https://www.codingame.com/playgrounds/6723/applying-hateoas-to-a-rest-api-with-spring-boot)
    * [What is "_embedded" in the request for collections?](https://stackoverflow.com/questions/27405637/meaning-and-usage-of-embedded-in-hateoas/27424771)
    * [Why isn't .toCollectionModel working?](https://stackoverflow.com/questions/60635680/understanding-spring-hateoas-error-embedded-wrapper-returned-null-for-both-the)
  * Testing
    * [Getting started with Spring controller testing](https://spring.io/guides/gs/testing-web/)
    * [JSON - Deserialising arrays](https://www.baeldung.com/jackson-collection-array)
* Vue
  * [Style guide](https://vuejs.org/v2/style-guide/)
  * [Cross component communication](https://medium.com/js-dojo/component-communication-in-vue-js-ca8b591d7efa)
  * [Getting Vue to react to property deletion](https://stackoverflow.com/questions/50782129/deleting-an-object-by-key-doesnt-update-the-vue-component)
  * [Remember to unregister event listeners](https://www.fatalerrors.org/a/remember-to-remove-the-event-listener-bound-with-on-in-vue.html)

# Instructions
## Requirements
* Install [Java Development Kit 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) for your platform
* Install [nodejs v15.14.0](https://nodejs.org/en/)

## Backend
* Open folder **backend_java_springboot** in IntelliJ and let it import the Maven project
* Edit parent pom.xml and change **build.target.directory** to a build directory of your choosing
* Compile the **service** module
* Go to Run -> Edit Configurations -> Add **Application** and set the following:
  * Main class: `com.idm.service.ServiceApplication`
  * Use classpath of module: `service`
* Run the configuration you just created
* Open `http://localhost:8080/packages` in any browser to see a list of packages available

## Frontend
* `cd frontend_vue_bootstrap`
* Run `npm install`
* Run `npm run serve`
* Open `http://localhost:8081/` to see the Home page. You won't see any packages unless the backend is up and running

# Architecture
## Backend
* `assemblers` - Build models that are surfaced to client via REST responses
* `controllers` - Standard Spring controllers, one for each API
* `models`
  * `data` - Internal POJOs
  * `resources` - Client facing POJOs
* `services` - Spring @Service classes, assemble backend data into internal POJOs
* `adapters` - Clients for talking to dependencies
### API
* `/packages`
  * GET - returns all packages
  * GET `/{id}` - returns single package
  * GET `/{id}/products` - returns products for a package
  * GET `/{packageId}/products/{productId}` - returns product in a package
  * DELETE `/{id}` - deletes a package
  * PUT `/{id}` - updates a package. Name and Description are updateable, other changes are currently ignored
  * POST - creates a new package with 0 products. Id will be generated automatically
* `/products`
  * GET - returns all products
  * GET `/{id}` - returns single product

## Frontend
* `components` - Vue components, 1 for each view. Header is global. ...View components are customer facing. ...Edit components are internal Admin views
* `service`
  * `BasketService` - Mock service for storing basket contents
  * `ProductPackageService` - Working service that calls Backend
  * `UserPersonalisationService` - Mock service for storing currency selection
* `events.js` - Constants for events used
* `routes.js` - Navigation setup

# Known issues
## Backend
* TODO comments mention most limitations
* When the server is run for the first time, it takes a few seconds to get the Product information. The frontend will look blank for a few seconds and then react
* There are no JavaDocs
* Code coverage limits not defined but unit test coverage is reasonably high
* Thread safety has not been tested

## Frontend
* The styling works for desktop for customer facing pages. However, they do not look nice when resizing, although the pages are functional - need to add proper breakpoints to make this work for mobile
* Code duplication in certain areas, needs some refactoring to create smaller components rather than just focusing on each View individually
* Cannot add products to a package in Admin mode
* Cannot change prices of products in Admin mode
* No security around Admin mode
* No double-check when deleting packages
* Does not have full accessibility (i.e. aria tags for everything)
* Does not have localisation (e.g. currency delimiter does not change to match currency)
* Cross browser compatibility unknown
* Discounting and Basket storing are done on the client side as a shortcut. Client should be doing as little logic as possible
* Should be more proper JavaScript classes for some of the modelling
* Revisiting Home by changing the URL in the browser will result in loss of cart information. Pressing Back works fine. You can use this bug to reset your cart to start a different test case
* No automated tests. Needs things like Selenium tests to make sure things are displayed correctly
* Promises should have error logging
* Long strings will not truncate if they cannot wrap

# Manual test cases
## Home
* Search
  * Check packages change
  * Check case insensitive
  * Deleting search will undo filter
* Sorting
  * Check all sort options work
* Navigation
  * Check package info is accessible via button (and is correct for that package)
  * Check pressing Home button does not cause a log error (can't navigate to same page)
* Basket
  * Check package can be added to cart via button
  * Check basket value updates when package is added
## Package details (reachable via Details button on Home)
* Info
  * Check Name, Description, Contents and Price are correct
* Basket
  * Check package can be added to cart via button
  * Check basket value updates when package is added
## Basket (reachable via Basket button on Header)
* Info
  * Check cart shows items added from other screens
  * Check cart shows Name, Individual price and Quantity of package
  * Check message is shown if no items are present in cart ("Nothing in basket")
  * Check Total displays correct value
* Navigation
  * Check pressing Basket button does not cause a log error (can't navigate to same page)
* Events
  * Pressing the plus button increases the Quantity by 1 and updates the Basket and Total
  * Pressing the minus button decreases the Quantity by 1 and updates the Basket and Total
  * Discounting:
    * Reducing package bought to a single package stops discounting being shown in Total and Basket
    * Increasing package bought above a single package causes discounting to be shown in Total and Basket
    * Check Total shows discounted and original value
    * Check Total shows reason for discount
## Header
* Contents
  * Check has Home button, Admin button, Title, Currency selection, Basket button
* Navigation
  * Check Home button takes you to index
  * Check Admin button takes you to Admin page
  * Check Basket button takes you to Basket page
* Events
  * Check adding more than 1 package triggers Basket button to show discount (discounted value and original value) from:
    * Home view
    * Package details view
  * Check Currency selection triggers price and symbol changes in:
    * Home view
    * Package details view
    * Basket view
* State
  * Check navigating forwards and backwards between Home view, Package details view and Basket view remembers Currency selection
## Admin (reachable via Admin button in Header)
* Info
  * Check Id, Name, Description, Update button and Delete button shown
* Navigation
  * Check Update button takes you to Package edit view for that package
  * Check Add button takes you to New package view
* State
  * Check delete deletes the package
  * Check deleting a package that's in the cart removes it from the cart (sub-test 1: 1 item in cart, sub-test 2: > 1 item in cart)
  * Check all packages can be deleted
  * (will need to restart Backend to recover from this test if you want the old packages)
## Add package view (reachable via Add button on Admin page)
* Info
  * Check Name and Description can be seen (nothing else)
  * Check Name and Description are editable
  * Check Name and Description are initially blank
* Validation
  * Check Name cannot be blank
  * Check Name must be more than 5 characters
  * Check Description cannot be blank
  * Check Description must be more than 5 characters
* Storage
  * Check Save button saves package to Backend AND navigates you back to Admin page
  * Check package is saved with correct information
  * Check newly added packages can be added to cart (they will have 0 products so will cost nothing)
  * Check newly added packages can be updated
  * Check newly added packages can be deleted
## Update package view (reachable via Update button on Admin page)
* Info
  * Check Id, Name, Description and Products are viewable
  * Check only Name and Description are editable
* Validation
  * Check Name cannot be blank
  * Check Name must be more than 5 characters
  * Check Description cannot be blank
  * Check Description must be more than 5 characters
* Storage
  * Check Save button saves package to Backend AND navigates you back to Admin page
  * Check package is saved with correct information
  * Check updating a package that's in the cart updates the cart item on navigation
## Exploratory testing
* Open Developer tools and navigate around website, keeping an eye out for interesting logs and errors
