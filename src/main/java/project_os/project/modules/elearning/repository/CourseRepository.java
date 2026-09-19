package project_os.project.modules.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_os.project.modules.elearning.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE " +
           "LOWER(c.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.student) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.favourite) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY c.id DESC")
    List<Course> searchCourses(@Param("keyword") String keyword);

    List<Course> findAllByOrderByIdDesc();
}
