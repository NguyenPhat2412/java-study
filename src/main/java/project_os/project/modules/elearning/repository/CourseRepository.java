package project_os.project.modules.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_os.project.modules.elearning.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> searchCourses(@Param("keyword") String keyword);

    List<Course> findAllByOrderByIdDesc();
}
