package project_os.project.modules.elearning.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project_os.project.modules.elearning.common.CoreContants;
import project_os.project.modules.elearning.common.SystemException;
import project_os.project.modules.elearning.dao.course.CourseDAO;
import project_os.project.modules.elearning.service.CourseService;
import project_os.project.modules.elearning.model.Course;
import project_os.project.modules.elearning.wrapper.CourseWrapper;

import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO;

    public CourseServiceImpl(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseWrapper> getCourses(String keyword) {
        List<Course> courses;
        if (keyword != null && !keyword.trim().isEmpty()) {
            courses = courseDAO.searchCourses(keyword.trim());
        } else {
            courses = courseDAO.findAll();
        }
        return courses.stream().map(CourseWrapper::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseWrapper getCourseById(Long id) {
        Course course = findCourseOrThrow(id);
        return CourseWrapper.fromEntity(course);
    }

    @Override
    public CourseWrapper createCourse(CourseWrapper wrapper) {
        validate(wrapper);
        Course course = new Course();
        mapWrapperToEntity(wrapper, course);
        Course saved = courseDAO.save(course);
        return CourseWrapper.fromEntity(saved);
    }

    @Override
    public CourseWrapper updateCourse(Long id, CourseWrapper wrapper) {
        Course course = findCourseOrThrow(id);
        validate(wrapper);
        mapWrapperToEntity(wrapper, course);

        Course updated = courseDAO.save(course);
        return CourseWrapper.fromEntity(updated);
    }

    @Override
    public CourseWrapper patchCourse(Long id, CourseWrapper wrapper) {
        Course course = findCourseOrThrow(id);
        if (wrapper == null) {
            throw SystemException.badRequest("Dữ liệu không được để trống");
        }
        if (wrapper.getDepartment() != null && !wrapper.getDepartment().trim().isEmpty()) {
            course.setDepartment(wrapper.getDepartment().trim());
        }
        if (wrapper.getStudent() != null && !wrapper.getStudent().trim().isEmpty()) {
            course.setStudent(wrapper.getStudent().trim());
        }
        if (wrapper.getFavourite() != null) {
            course.setFavourite(wrapper.getFavourite().trim());
        }
        if (wrapper.getIsStatus() != null) {
            course.setIsStatus(wrapper.getIsStatus());
        }
        Course updated = courseDAO.save(course);
        return CourseWrapper.fromEntity(updated);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = findCourseOrThrow(id);
        courseDAO.delete(course);
    }

    private void validate(CourseWrapper wrapper) {
        if (wrapper == null) {
            throw SystemException.badRequest("Dữ liệu không được để trống");
        }
        if (wrapper.getDepartment() == null || wrapper.getDepartment().trim().isEmpty()) {
            throw SystemException.badRequest("Khoa (Department) không được để trống");
        }
        if (wrapper.getStudent() == null || wrapper.getStudent().trim().isEmpty()) {
            throw SystemException.badRequest("Tên sinh viên (Student) không được để trống");
        }
    }

    private void mapWrapperToEntity(CourseWrapper wrapper, Course course) {
        course.setDepartment(wrapper.getDepartment().trim());
        course.setStudent(wrapper.getStudent().trim());
        if (wrapper.getFavourite() != null) {
            course.setFavourite(wrapper.getFavourite().trim());
        }
        if (wrapper.getIsStatus() != null) {
            course.setIsStatus(wrapper.getIsStatus());
        }
        if (course.getFavourite() == null) {
            course.setFavourite("");
        }
        if (course.getIsStatus() == null) {
            course.setIsStatus(true);
        }
    }

    private Course findCourseOrThrow(Long id) {
        return courseDAO.findById(id)
                .orElseThrow(() -> SystemException.notFound(CoreContants.COURSE_NOT_FOUND + " với ID: " + id));
    }
}
