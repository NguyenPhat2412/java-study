package project_os.project.modules.elearning.service;

import project_os.project.modules.elearning.wrapper.CourseWrapper;

import java.util.List;

public interface CourseService {
    List<CourseWrapper> getCourses(String keyword);
    CourseWrapper getCourseById(Long id);
    CourseWrapper createCourse(CourseWrapper courseWrapper);
    CourseWrapper updateCourse(Long id, CourseWrapper courseWrapper);
    CourseWrapper patchCourse(Long id, CourseWrapper courseWrapper);
    void deleteCourse(Long id);
}