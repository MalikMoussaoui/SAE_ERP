package com.unilim.erp.service;

import com.unilim.erp.entities.Course;
import com.unilim.erp.repositories.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(UUID id) {
        return courseRepository.findById(id);
    }

    public Course create(Course course) {
        return courseRepository.save(course);
    }

    public Course update(UUID id, Course courseDetails) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setHours(courseDetails.getHours());
                    course.setResourceSheet(courseDetails.getResourceSheet());
                    course.setSae(courseDetails.getSae());
                    course.setResource(courseDetails.getResource());

                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new RuntimeException("Cours introuvable avec l'ID : " + id));
    }

    public void delete(UUID id) {
        courseRepository.deleteById(id);
    }
}