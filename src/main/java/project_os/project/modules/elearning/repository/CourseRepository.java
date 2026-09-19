package project_os.project.modules.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_os.project.modules.elearning.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Spring Data JPA sẽ tự động liên kết với @NamedQuery("Course.searchCourses") được định nghĩa trong Course entity
    List<Course> searchCourses(@Param("keyword") String keyword);

    List<Course> findAllByOrderByIdDesc();
}
