package project_os.project.modules.elearning.dao.course;

import project_os.project.modules.elearning.model.Course;

import java.util.List;
import java.util.Optional;

/**
 * Abstraction persistence của Course.
 *
 * Service phụ thuộc interface này thay vì EntityManager. Đây là Dependency
 * Inversion: business layer không phụ thuộc implementation database và có
 * thể test bằng mock DAO.
 */
public interface CourseDAO {
    List<Course> findAll();
    Optional<Course> findById(Long id);
    List<Course> searchCourses(String keyword);
    Course save(Course course);
    void delete(Course course);
    void deleteById(Long id);
    boolean existsById(Long id);
}
