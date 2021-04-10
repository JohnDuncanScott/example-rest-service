<template>
  <div class="container">
    <h3>All Courses</h3>
    <div v-if="message" class="alert alert-success">{{this.message}}</div>
    <div class="container">
      <table class="table">
        <thead>
          <tr>
            <th>Id</th>
            <th>Description</th>
            <th>Update</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="course in courses" v-bind:key="course.id">
            <td>{{course.id}}</td>
            <td>{{course.description}}</td>
            <td><button class="btn btn-success" v-on:click="updateCourseClicked(course.id)">Update</button></td>
            <td><button class="btn btn-warning" v-on:click="deleteCourseClicked(course.id)">Delete</button></td>
          </tr>
        </tbody>
        <div class="row">
          <button class="btn btn-success" v-on:click="addCourseClicked()">Add</button>
        </div>
      </table>
    </div>
  </div>
</template>

<script>
import CourseDataService from '../service/CourseDataService';
export default {
  name: "CoursesList",
  data() {
    return {
      courses: [],
      message: "",
      INSTRUCTOR: "in28minutes"
    };
  },
  methods: {
    refreshCourses() {
      CourseDataService.retrieveAllCourses(this.INSTRUCTOR)
        .then(response => {
          this.courses = response.data;
        });
    },
    deleteCourseClicked(id) {
      CourseDataService.deleteCourse(this.INSTRUCTOR, id)
      .then(() => {
        this.message = `Deleting course ${id} was successful`;
        this.refreshCourses();
      });
    },
    updateCourseClicked(id) {
      this.$router.push(`/courses/${id}`);
    },
    addCourseClicked() {
      this.$router.push(`/courses/-1`);
    }
  },
  created() {
    this.refreshCourses();
  }
};
</script>

<style>
</style>