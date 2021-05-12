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

# Steps to productionize
## Backend
* ExchangeRateClientAdapterImpl
  * Add unit tests
  * Query common currencies all in 1 request to avoid multiple trips
  * Move access key to a secure secrets store (e.g. [AWS Secrets Manager](https://aws.amazon.com/secrets-manager/)) (or at least a props file)
  * Create proper model for API response
  * Don't swallow all exceptions
* ProductClientAdapterImpl
  * Add unit tests
  * Inject ObjectMapper
  * Move cache to parent service, doesn't belong in adapter. Also use proper cache with TTL (e.g. [Guava cache](https://www.baeldung.com/guava-cache))
  * Move user name and password to a secure secrets store (e.g. [AWS Secrets Manager](https://aws.amazon.com/secrets-manager/)) (or at least a props file)
* ProductPackageClientAdapterImpl
  * Move product package definitions to a database and read / write using [Spring Data JPA + Hibernate](https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa) or similar. Use auto-incrementing id for product package id
* ProductPackageResourceAssembler
  * Add full unit tests
  * Replace "@Nullable ProductPackageInstant originalProductPackageInstant" with Optional
  * Fix self link with param hack properly
* ProductController
  * Make HATEOS / HAL compliant
  * Use different model for return values
* ProductPackageController
  * Setup proper error page
  * Inject ProductPackageResourceAssembler
  * Replace optional params with Optionals
  * Auto-convert currency params to Currency instances
* Models
  * Add unit tests
  * Check backend and frontend validation matches up
* ExchangeRateServiceImpl
  * Use proper cache with TTL (e.g. [Guava cache](https://www.baeldung.com/guava-cache))
  * If backup currency conversion were to be kept, this would be more maintainable as a Map rather than a switch statement. Already have a cache, so could just populate with defaults
* ProductPackageServiceImpl
  * Use Optional rather than returning null, would make API much cleaner
  * Separate out price conversions to a different class
* ProductServiceImpl
  * Use Optional rather than returning null, would make API much cleaner
  * Should be using a DTO and not leaking the Product model higher up the stack
* General
  * Number of packages will grow, so should use paging properly in REST API - [PagedResourcesAssembler](https://stackoverflow.com/questions/21346387/how-to-correctly-use-pagedresourcesassembler-from-spring-data)
  * Packages and Products should have an audit history and should be able to rollback to a previous version
  * Use constructor injection where missing (`@RequiredArgsConstructor(onConstructor = @__(@Inject))`) (missing this in the adapters for example)
  * Check using final where appropriate (not using it enough at the moment)
  * JavaDocs for all the classes
  * Add proper trackable metric counters
  * Where appropriate, use something like [MapStruct](https://stackabuse.com/guide-to-mapstruct-in-java-advanced-mapping-library/) for DTO mapping. Also see [performance of mapping frameworks](https://www.baeldung.com/java-performance-mapping-frameworks)
  * Convert money handling to use [org.javamoney](https://www.baeldung.com/java-money-and-currency)
  * Auto-generate documentation using [Spring REST Docs](https://www.baeldung.com/spring-rest-docs)
  * Enforce code coverage thresholds
  * Enable style checks (CheckStyle)
  * Enable static analysis checks (FindBugs)
  * Add functional tests (headless + tailess tests)
  * Add integration tests
  * Add performance tests (soak and load)
  * Check G1 GC is being used
  * Full CI/CD system out of scope :)

## Frontend
* BasketView
  * Clicking on a basket item should take you to the details for it
* ProductPackageEdit
  * Secure editing functionality
  * Move "new" to constant
* ProductPackagesEdit
  * Remove redundant ".then();"
  * Move "new" to constant
  * Delete should have a warning dialog
* ProductPackagesView
  * Move search functionality to backend. Although it works for a small number of items like this, really the search param should be passed to the backend. Also need paging. To reduce the load on the backend, search should be a positive confirmation rather than the dynamic searching that currently happens
  * Change case-insensitive search to use toUpperCase instead of toLowerCase to avoid round-tripping problems in countries like Turkey <https://garygregory.wordpress.com/2015/11/03/java-lowercase-conversion-turkey/>
* BasketService
  * This is a hack to keep basket state. Would likely be a whole service that included handling purchasing. This API would either fit in there or if warranted would be its own microservice. This type of data is more suited to a NoSQL database where we want high availability (e.g. DynamoDb which is actually [used for Amazon Cart](https://www.dynamodbguide.com/the-dynamo-paper))
* UserPersonalisationService
  * This a hack to keep user's preferred options. Would likely be its own microservice that kept track of all forms of user preferences
* General
  * JavaScript
    * Add error functions to promises where appropriate
    * Create proper types where interacting with the APIs using ES6 class functionality
    * Add more documentation
    * Move currency symbol and how to display to backend to reduce logic in client
    * Add proper trackable metric counters
  * CSS
    * Use [SASS or LESS](https://templeton.io/less-vs-sass-scss-why-which/) to create cleaner and more modular stylesheets
  * Vue
    * Check Vue best practice documentation to make sure it's being followed
    * Componentize some of the common buttons rather than re-declaring them
    * Check for and fix any alignment issues
  * Automated testing
    * Add unit tests
    * Enable JSLint
    * Selenium UI tests
    * Performance / Responsiveness tests
  * Safety
    * Ensure strict mode is activated
    * Replace var with let - [var vs. let](https://www.educba.com/javascript-var-vs-let/)
    * Switch to TypeScript
  * Security
    * Would of course need a login for user
    * Admin website should be completely independent to retail website
  * Performance
    * Use Audit functionality in Dev Tools to assess website
    * Make sure text compression is enabled
    * Enable minification
    * No images in this project, but you'd also make sure the correct image sizes were being used
  * Localisation
    * Check time formats, money formats, etc.
    * Check no hardcoded user facing strings - should only use constants that map to localised values. Can also use [pseudolocalisation](https://en.wikipedia.org/wiki/Pseudolocalization) to help detect problems
  * Compatibility
    * Check compatibility across browsers
    * Check breakpoints for site to ensure displays fine on mobile
    * Check on selection of real devices as a safety check (can use some kind of device farm, e.g. [AWS Device Farm](https://aws.amazon.com/device-farm/))
  * Accessibility
    * Check accessibility - aria tags, use screen reader to tab through and check it makes sense
    * Use [colour contrast tool](https://snook.ca/technical/colour_contrast/colour.html) to check colours picked are good 
    * Check [colour blindness](http://www.particletree.com/features/interfaces-and-color-blindness)
    * Check [WCAG guidelines](https://www.w3.org/WAI/standards-guidelines/wcag/glance/)
    * Use something like [NoCoffee](https://accessgarage.wordpress.com/2013/02/09/458/) to test the website