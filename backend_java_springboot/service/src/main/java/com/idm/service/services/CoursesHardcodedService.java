package com.idm.service.services;

import com.idm.service.models.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CoursesHardcodedService {
    private static List<Course> courses = new ArrayList<>();
    private static long idCounter = 0;

    static {
        courses.add(new Course(++idCounter, "in28minutes", "Learn Full stack with Spring Boot and Angular"));
        courses.add(new Course(++idCounter, "in28minutes", "Learn Full stack with Spring Boot and React"));
        courses.add(new Course(++idCounter, "in28minutes", "Master Microservices with Spring Boot and Spring Cloud"));
        courses.add(new Course(++idCounter, "in28minutes","Deploy Spring Boot Microservices to Cloud with Docker and Kubernetes"));
    }

    public List<Course> findAll() {
        log.info("Found courses, size: {}", courses.size());
        return courses;
    }

    public Course findById(long id) {
        for (Course course : courses) {
            if (course.getId() == id) {
                log.info("Course found: {}", course);
                return course;
            }
        }

        logNoCourseFound(id);
        return null;
    }

    public Course deleteById(long id) {
        Course course = findById(id);

        if (course == null)
            return null;

        if (courses.remove(course)) {
            log.info("Removed course, id: {}", id);
            return course;
        }

        log.info("Could not remove course, id: {}", id);
        return null;
    }

    public Course save(Course course) {
        if (course.getId() == -1 || course.getId() == 0) {
            course.setId(++idCounter);
            courses.add(course);
            log.info("Added new course: {}", course);
        } else {
            deleteById(course.getId());
            courses.add(course);
            log.info("Added course: {}", course);
        }
        return course;
    }

    private void logNoCourseFound(long id) {
        log.info("No course found for id: {}", id);
    }
}
