package project_os.project.modules.elearning.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import project_os.project.modules.elearning.dto.CourseRequest;
import project_os.project.modules.elearning.dto.CourseResponse;
import project_os.project.modules.elearning.model.Course;
import project_os.project.modules.elearning.repository.CourseRepository;

import java.util.List;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCourses(String keyword) {
        List<Course> courses;
        if (keyword != null && !keyword.trim().isEmpty()) {
            courses = courseRepository.searchCourses(keyword.trim());
        } else {
            courses = courseRepository.findAllByOrderByIdDesc();
        }
        return courses.stream().map(CourseResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        Course course = findCourseOrThrow(id);
        return CourseResponse.fromEntity(course);
    }

    public CourseResponse createCourse(CourseRequest request) {
        if (request.department() == null || request.department().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoa (Department) không được để trống");
        }
        if (request.student() == null || request.student().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên sinh viên (Student) không được để trống");
        }

        Course course = new Course(
                request.department().trim(),
                request.student().trim(),
                request.favourite() != null ? request.favourite().trim() : "",
                request.isStatus() != null ? request.isStatus() : true
        );
        Course saved = courseRepository.save(course);
        return CourseResponse.fromEntity(saved);
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = findCourseOrThrow(id);
        if (request.department() == null || request.department().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoa (Department) không được để trống");
        }
        if (request.student() == null || request.student().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên sinh viên (Student) không được để trống");
        }
        course.setDepartment(request.department().trim());
        course.setStudent(request.student().trim());
        course.setFavourite(request.favourite() != null ? request.favourite().trim() : "");
        course.setIsStatus(request.isStatus() != null ? request.isStatus() : true);

        Course updated = courseRepository.save(course);
        return CourseResponse.fromEntity(updated);
    }

    public CourseResponse patchCourse(Long id, CourseRequest request) {
        Course course = findCourseOrThrow(id);
        request.applyTo(course);
        Course updated = courseRepository.save(course);
        return CourseResponse.fromEntity(updated);
    }

    public void deleteCourse(Long id) {
        Course course = findCourseOrThrow(id);
        courseRepository.delete(course);
    }

    private Course findCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy dữ liệu với ID: " + id));
    }
}
