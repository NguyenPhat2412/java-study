package project_os.project.modules.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project_os.project.modules.elearning.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY s.id DESC")
    List<Student> findByKeyword(@Param("keyword") String keyword);

    List<Student> findAllByOrderByIdDesc();

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);
}

